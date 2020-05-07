# Infrastructure as code using [@aws-cdk](https://github.com/aws/aws-cdk)

Examples on how to provision an AWS infrastructure using [@aws-cdk](https://github.com/aws/aws-cdk).

| Example | Description |
|---------|-------------|
| [ec2](ec2) | Provision a single EC2 instance with a running nginx server |
| [vpc](vpc)| Provision a Virtual Private Cloud (VPC) |

## Useful commands
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Command&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; | Description |
| ------- | ------------ |
| `mvn clean install`  | must be on the root directory |
| `cdk bootstrap` | to deploy the CDK toolkit stack into an AWS environment [within any of the sub directory] |
| `cdk synth` | to synthesizes and prints the CloudFormation template for this stack [within the sub directory of the stack you want to synthesize] |
| `cdk deploy` | to deploy stack into your AWS account [within the sub directory of the stack you want to deploy] |
