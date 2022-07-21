package z8.mctrl.db.jooq.generator;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;
import static org.jooq.impl.DSL.*;
import org.jooq.*;
import org.jooq.Record; // Starting with Java 14, Record can not be imported on demand anymore
import org.jooq.impl.*;

public final class CustomJooqGenerator extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        if (mode == Mode.POJO) return definition.getInputName() + "Object";
        if (mode == Mode.DAO) return definition.getInputName() + "Dao";
        if (mode == Mode.RECORD) return definition.getInputName() + "Record";
        return super.getJavaClassName(definition, mode);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return "call" + org.jooq.tools.StringUtils.toCamelCase(definition.getOutputName());
    }



}