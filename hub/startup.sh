#!/usr/bin/env bash

#launch hub
response=`aws ec2 run-instances \
    --image-id ami-759bc50a \
    --count 1 \
    --instance-type t2.small \
    --key-name max \
    --security-group-ids sg-0ed8b51b34bd0885e \
    --subnet-id subnet-bfb2c2f7 \
    --instance-initiated-shutdown-behavior terminate \
    --associate-public-ip-address`
hubId=$(echo $response | jq -r '.Instances [] .InstanceId')
aws ec2 create-tags \
    --resources $hubId \
    --tags \
        'Key="Name",Value="Foundations of Selenium - Hub"' \
        'Key="Group",Value="Foundations of Selenium"'

#launch chrome node
response=`aws ec2 run-instances \
    --image-id ami-759bc50a \
    --count 1 \
    --instance-type t2.small \
    --key-name max \
    --security-group-ids sg-0ed8b51b34bd0885e \
    --subnet-id subnet-bfb2c2f7 \
    --instance-initiated-shutdown-behavior terminate \
    --associate-public-ip-address`
chromeNodeId=$(echo $response | jq -r '.Instances [] .InstanceId')
aws ec2 create-tags \
    --resources $chromeNodeId \
    --tags \
        'Key="Name",Value="Foundations of Selenium - Chrome Node"' \
        'Key="Group",Value="Foundations of Selenium"'

#launch firefox node
response=`aws ec2 run-instances \
    --image-id ami-759bc50a \
    --count 1 \
    --instance-type t2.small \
    --key-name max \
    --security-group-ids sg-0ed8b51b34bd0885e \
    --subnet-id subnet-bfb2c2f7 \
    --instance-initiated-shutdown-behavior terminate \
    --associate-public-ip-address`
firefoxNodeId=$(echo $response | jq -r '.Instances [] .InstanceId')
aws ec2 create-tags \
    --resources $firefoxNodeId \
    --tags \
        'Key="Name",Value="Foundations of Selenium - Firefox Node"' \
        'Key="Group",Value="Foundations of Selenium"'

#wait for hub to be available
details=`aws ec2 describe-instances \
    --instance-ids $hubId`
state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
while [ "$state" -ne 16 ]; do
  sleep 10
  details=`aws ec2 describe-instances \
      --instance-ids $hubId`
  state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
done
hubIp=$(echo $details | jq -r '.Reservations [] .Instances [] .PublicIpAddress')
while ! ssh -oStrictHostKeyChecking=no ubuntu@$hubIp 'echo'
do
    sleep 10
done

#wait for chrome node to be available
details=`aws ec2 describe-instances \
    --instance-ids $chromeNodeId`
state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
while [ "$state" -ne 16 ]; do
  sleep 10
  details=`aws ec2 describe-instances \
      --instance-ids $chromeNodeId`
  state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
done
chromeNodeIp=$(echo $details | jq -r '.Reservations [] .Instances [] .PublicIpAddress')
while ! ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp 'echo'
do
    sleep 10
done

#wait for firefox node to be available
details=`aws ec2 describe-instances \
    --instance-ids $firefoxNodeId`
state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
while [ "$state" -ne 16 ]; do
  sleep 10
  details=`aws ec2 describe-instances \
      --instance-ids $firefoxNodeId`
  state=$(echo $details | jq -r '.Reservations [] .Instances [] .State .Code')
done
firefoxNodeIp=$(echo $details | jq -r '.Reservations [] .Instances [] .PublicIpAddress')
while ! ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp 'echo'
do
    sleep 10
done

#setup and configure hub
ssh -oStrictHostKeyChecking=no ubuntu@$hubIp 'sudo apt-get -qq update; sudo apt-get -yqq upgrade; sudo apt-get -yqq install default-jdk; wget -q https://selenium-release.storage.googleapis.com/3.14/selenium-server-standalone-3.14.0.jar;'
ssh -oStrictHostKeyChecking=no ubuntu@$hubIp 'java -jar selenium-server-standalone-3.14.0.jar -role hub > /dev/null 2>&1 &'
#setup and chrome node
# TODO - install chrome
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp 'sudo apt-get -qq update; sudo apt-get -yqq upgrade; sudo apt-get -yqq install default-jdk; wget -q https://selenium-release.storage.googleapis.com/3.14/selenium-server-standalone-3.14.0.jar;'
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp "java -jar selenium-server-standalone-3.14.0.jar -role node -hub http://$hubIp:4444/grid/register -browser browserName=chrome,maxInstances=5 > /dev/null 2>&1 &"
#setup and firefox node
ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp 'sudo apt-get -qq update; sudo apt-get -yqq upgrade; sudo apt-get -yqq install default-jdk; sudo apt-get -yqq install firefox; wget -q https://selenium-release.storage.googleapis.com/3.14/selenium-server-standalone-3.14.0.jar;'
ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp "java -jar selenium-server-standalone-3.14.0.jar -role node -hub http://$hubIp:4444/grid/register -browser browserName=firefox,maxInstances=5 > /dev/null 2>&1 &"
#setup and firefox node
# TODO - setup and install internet explorer node
echo
echo
echo
echo "Access Selenium Hub at http://$hubIp:4444/grid/console"
