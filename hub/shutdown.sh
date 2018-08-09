#!/usr/bin/env bash

details=`aws ec2 describe-instances \
    --filters "Name=tag:Group,Values=Fundamentals of Selenium"`
instances=$(echo $details | jq -r '.Reservations [] .Instances [] .InstanceId')
status=`aws ec2 terminate-instances \
    --instance-ids $instances`
