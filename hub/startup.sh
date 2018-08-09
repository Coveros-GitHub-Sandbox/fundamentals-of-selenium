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
        'Key="Name",Value="Fundamentals of Selenium - Hub"' \
        'Key="Group",Value="Fundamentals of Selenium"'

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
        'Key="Name",Value="Fundamentals of Selenium - Chrome Node"' \
        'Key="Group",Value="Fundamentals of Selenium"'

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
        'Key="Name",Value="Fundamentals of Selenium - Firefox Node"' \
        'Key="Group",Value="Fundamentals of Selenium"'

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

selenium=3.14

#setup and configure hub
ssh -oStrictHostKeyChecking=no ubuntu@$hubIp "sudo su; apt-get -qq update; apt-get -qqy upgrade; apt-get -qqy install default-jdk"
ssh -oStrictHostKeyChecking=no ubuntu@$hubIp "wget -q https://selenium-release.storage.googleapis.com/$selenium/selenium-server-standalone-$selenium.0.jar"
ssh -oStrictHostKeyChecking=no ubuntu@$hubIp "java -jar selenium-server-standalone-$selenium.0.jar -role hub > /dev/null 2>&1 &"
#setup and configure chrome node
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp "sudo su; echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' >> /etc/apt/sources.list; wget https://dl.google.com/linux/linux_signing_key.pub; apt-key add linux_signing_key.pub"
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp "sudo su; apt-get -qq update; apt-get -qqy upgrade; apt-get -qqy install default-jdk; apt-get -qqy install google-chrome-stable; apt-get -qqy install xvfb"
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp "wget -q https://selenium-release.storage.googleapis.com/$selenium/selenium-server-standalone-$selenium.0.jar; wget -q https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip; unzip chromedriver_linux64.zip"
ssh -oStrictHostKeyChecking=no ubuntu@$chromeNodeIp "export DISPLAY=:99; Xvfb :99 & java -Dwebdriver.chrome.driver=chromedriver -jar selenium-server-standalone-$selenium.0.jar -role node -hub http://$hubIp:4444/grid/register -browser browserName=chrome,maxInstances=5 > /dev/null 2>&1 &"
#setup and configure firefox node
ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp "sudo su apt-get -qq update; apt-get -qqy upgrade; apt-get -qqy install default-jdk; apt-get -qqy install firefox; apt-get -qqy install xvfb; apt-get -qqy install dbus-x11;"
ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp "wget -q https://selenium-release.storage.googleapis.com/$selenium/selenium-server-standalone-$selenium.0.jar; wget -q https://github.com/mozilla/geckodriver/releases/download/v0.21.0/geckodriver-v0.21.0-linux64.tar.gz; tar -xzf geckodriver-v0.21.0-linux64.tar.gz"
ssh -oStrictHostKeyChecking=no ubuntu@$firefoxNodeIp "export DISPLAY=:99; Xvfb :99 & java -Dwebdriver.gecko.driver=geckodriver -jar selenium-server-standalone-$selenium.0.jar -role node -hub http://$hubIp:4444/grid/register -browser browserName=firefox,maxInstances=5 > /dev/null 2>&1 &"
#setup and configure internet explorer node
# TODO
echo
echo
echo
echo "Access Selenium Hub at http://$hubIp:4444/grid/console"
echo "Run tests using Hub endpoint http://$hubIp:4444"
