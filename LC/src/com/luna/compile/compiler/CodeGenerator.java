package com.luna.compile.compiler;

import com.luna.base.config.Config;
import com.luna.compile.constant.COMPONENT;
import com.luna.compile.struct.Context;

public class CodeGenerator extends Component {

    private CodeGenerator() {}

    private static Component instance;

    @Override
    public COMPONENT type() {
        return COMPONENT.CODE_GENERATOR;
    }

    public static Component getInstance() {
        if(instance == null) instance = new CodeGenerator();
        return instance;
    }

    @Override
    public Component run(Context context, Config config) {
        this.context = context;
        return this;
    }

}
