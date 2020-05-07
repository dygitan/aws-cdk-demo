package dygitan.demo.aws.vpc;

import dygitan.demo.aws.shared.SharedStack;
import software.amazon.awscdk.core.Construct;

public class AppStack extends SharedStack {

    public AppStack(final Construct scope, final String id, final String name) {
        super(scope, id, name);
    }
}
