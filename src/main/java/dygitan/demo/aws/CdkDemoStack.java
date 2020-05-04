package dygitan.demo.aws;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;

public class CdkDemoStack extends Stack {

    public CdkDemoStack(final Construct scope, final String id) {
        this(scope, id, StackProps.builder()
            .env(Environment.builder()
                .region("ap-southeast-2")
                .build())
            .build());
    }

    public CdkDemoStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        CfnVPC cfnVpc = CfnVPC.Builder.create(this, "DemoVpc")
            .cidrBlock("192.168.0.0/22")
            .enableDnsHostnames(true)
            .instanceTenancy("default")
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("vpc-demo")
                .build()))
            .build();

        CfnSubnet cfnSubnet = CfnSubnet.Builder.create(this, "DemoSubnet")
            .availabilityZone("ap-southeast-2a")
            .cidrBlock("192.168.0.0/24")
            .mapPublicIpOnLaunch(true)
            .vpcId(cfnVpc.getRef())
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("subnet-az-2a-demo")
                .build()))
            .build();

        CfnRouteTable cfnRouteTable = CfnRouteTable.Builder.create(this, "DemoRouteTable")
            .vpcId(cfnVpc.getRef())
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("rtb-demo")
                .build()))
            .build();

        CfnSubnetRouteTableAssociation.Builder.create(this, "DemoSubnetRouteTableLink")
            .routeTableId(cfnRouteTable.getRef())
            .subnetId(cfnSubnet.getRef())
            .build();

        CfnInternetGateway cfnInternetGateway = CfnInternetGateway.Builder.create(this, "DemoInternetGateway")
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("igw-demo")
                .build()))
            .build();

        CfnVPCGatewayAttachment.Builder.create(this, "DemoInternetGatewayAttachment")
            .internetGatewayId(cfnInternetGateway.getRef())
            .vpcId(cfnVpc.getRef())
            .build();

        CfnRoute.Builder.create(this, "DemoRoute")
            .routeTableId(cfnRouteTable.getRef())
            .gatewayId(cfnInternetGateway.getRef())
            .destinationCidrBlock("0.0.0.0/0")
            .build();

        CfnSecurityGroup cfnSecurityGroup = CfnSecurityGroup.Builder.create(this, "DemoSecurityGroup")
            .groupName("allow-http-ssh-traffic")
            .groupDescription("Allow web traffic on port 80 and ssh traffic on port 22")
            .securityGroupIngress(Arrays.asList(
                CfnSecurityGroup.IngressProperty.builder()
                    .cidrIp("0.0.0.0/0")
                    .description("Allow HTTP traffic")
                    .fromPort(80)
                    .toPort(80)
                    .ipProtocol("tcp")
                    .build(),
                CfnSecurityGroup.IngressProperty.builder()
                    .cidrIp("0.0.0.0/0")
                    .description("Allow SSH traffic")
                    .fromPort(22)
                    .toPort(22)
                    .ipProtocol("tcp")
                    .build()))
            .vpcId(cfnVpc.getRef())
            .build();

        UserData userData = UserData.forLinux(LinuxUserDataOptions.builder()
            .shebang("!/bin/bash")
            .build());
        userData.addCommands(
            "sudo apt-get update",
            "sudo apt-get install nginx -y",
            "sudo nginx"
        );

        CfnInstance.Builder.create(this, "DemoEc2Instance")
            .availabilityZone("ap-southeast-2a")
            .imageId("ami-0a1a4d97d4af3009b") // Ubuntu, 20.04 LTS
            .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO).toString())
            .keyName("ec2-demo-instance")
            .subnetId(cfnSubnet.getRef())
            .securityGroupIds(Arrays.asList(cfnSecurityGroup.getRef()))
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("demo-instance")
                .build()))
            .userData(Fn.base64(userData.render()))
            .build();
    }
}
