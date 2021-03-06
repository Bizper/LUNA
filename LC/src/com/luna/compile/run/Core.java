package com.luna.compile.run;

import com.luna.base.config.Config;
import com.luna.base.io.OUT;
import com.luna.base.manager.SecurityManager;
import com.luna.compile.compiler.*;
import com.luna.compile.constant.STATUS;
import com.luna.compile.struct.Context;

import java.util.ArrayList;
import java.util.List;

public class Core implements AbstractCore {

    private final List<Component> path = new ArrayList<>();

    public void launch(Config config) {
        if(config.isDebug()) OUT.openDebug();
        path.clear();
        path.add(Tokenizer.getInstance());
        path.add(TokenStreamChecker.getInstance());
        path.add(Preprocessor.getInstance());
        path.add(SyntaxProcessor.getInstance());
        path.add(Linker.getInstance());
        if(config.isGenerateBytecodeFile()) {
            path.add(CodeGenerator.getInstance());
        } else {
            path.add(Printer.getInstance());
        }
        SecurityManager securityManager = SecurityManager.getSecurityManager();
        if(!securityManager.check(path)) {
            OUT.err("Error Happened during checking the compile chain.");
            OUT.err("Check your options.");
            OUT.err(securityManager.getErrMsg());
            return;
        }
        this.run(config);
    }

    public void close() {
        path.clear();
    }

    public void run(Config config) {
        Context context = Context.get();
        for(Component component : path) {
            context = component.run(context, config).getContext();
            if(context.getCode() != STATUS.OK) {
                for(String err : context.getErrMsg()) {
                    OUT.err(err);
                }
                OUT.err(context);
                return;
            }
        }
        if(context.getCode() != STATUS.OK) {
            for(String err : context.getErrMsg()) {
                OUT.err(err);
            }
            OUT.err(context);
            return;
        }
        OUT.info(context);
        close();
    }


}
