package dygitan.demo.aws.shared;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;

public class SharedStack extends Stack {

    private final CfnVPC cfnVpc;
    private final CfnSubnet cfnSubnet;
    private final CfnRouteTable cfnRouteTable;
    private final CfnSecurityGroup cfnSecurityGroup;
    private final String logicalId;

    public SharedStack(final Construct scope, final String id, final String name) {
        this(scope, id, StackProps.builder()
            .env(Environment.builder()
                .region("ap-southeast-2")
                .build())
            .stackName(name)
            .build());
    }

    public SharedStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        this.logicalId = id;

        this.cfnVpc = CfnVPC.Builder.create(this, id.equals("VPCDemo") ? id : format("VPC"))
            .cidrBlock("192.168.0.0/22")
            .enableDnsHostnames(true)
            .instanceTenancy("default")
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("vpc-demo")
                .build()))
            .build();

        this.cfnSubnet = CfnSubnet.Builder.create(this, format("Subnet"))
            .availabilityZone("ap-southeast-2a")
            .cidrBlock("192.168.0.0/24")
            .mapPublicIpOnLaunch(true)
            .vpcId(cfnVpc.getRef())
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("subnet-az-2a-demo")
                .build()))
            .build();

        this.cfnRouteTable = CfnRouteTable.Builder.create(this, format("RouteTable"))
            .vpcId(cfnVpc.getRef())
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("rtb-demo")
                .build()))
            .build();

        CfnSubnetRouteTableAssociation.Builder.create(this, format("SubnetRouteTableLink"))
            .routeTableId(cfnRouteTable.getRef())
            .subnetId(cfnSubnet.getRef())
            .build();

        CfnInternetGateway cfnInternetGateway = CfnInternetGateway.Builder.create(this, format("InternetGateway"))
            .tags(Arrays.asList(CfnTag.builder()
                .key("Name")
                .value("igw-demo")
                .build()))
            .build();

        CfnVPCGatewayAttachment.Builder.create(this, format("InternetGatewayAttachment"))
            .internetGatewayId(cfnInternetGateway.getRef())
            .vpcId(cfnVpc.getRef())
            .build();

        CfnRoute.Builder.create(this, format("Route"))
            .routeTableId(cfnRouteTable.getRef())
            .gatewayId(cfnInternetGateway.getRef())
            .destinationCidrBlock("0.0.0.0/0")
            .build();

        this.cfnSecurityGroup = CfnSecurityGroup.Builder.create(this, format("SecurityGroup"))
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
    }

    public String format(String value) {
        return String.format("%s%s", this.logicalId, value);
    }

    public CfnRouteTable getCfnRouteTable() {
        return cfnRouteTable;
    }

    public CfnSecurityGroup getCfnSecurityGroup() {
        return cfnSecurityGroup;
    }

    public CfnSubnet getCfnSubnet() {
        return cfnSubnet;
    }

    public CfnVPC getCfnVpc() {
        return cfnVpc;
    }
}
