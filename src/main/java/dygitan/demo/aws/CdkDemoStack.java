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
    }
}
