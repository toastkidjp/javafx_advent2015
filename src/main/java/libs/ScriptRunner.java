package libs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import models.Language;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.python.jsr223.PyScriptEngineFactory;

/**
 * Script を実行.
 * @author Toast kid
 *
 */
public final class ScriptRunner {

    /**
     * run script.
     * @param script
     * @param language
     * @return result run script.
     */
    public Optional<String> runScript(final String script, final Language language) {
        if (StringUtils.isEmpty(script)) {
            return Optional.of("");
        }
        final ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
        final ScriptEngine python = new PyScriptEngineFactory().getScriptEngine();
        final ScriptEngine groovy = new GroovyScriptEngineFactory().getScriptEngine();

        final StringBuilder result = new StringBuilder();
        // redirect.
        final StringWriter writer = new StringWriter();
        try {
            if (js != null) {
                final ScriptContext context = js.getContext();
                context.setWriter(writer);
                context.setErrorWriter(writer);
            }
            if (python != null) {
                final ScriptContext context = python.getContext();
                context.setWriter(new PrintWriter(writer));
                context.setErrorWriter(new PrintWriter(writer));
            }
            if (groovy != null) {
                final ScriptContext context = groovy.getContext();
                context.setWriter(new PrintWriter(writer));
                context.setErrorWriter(new PrintWriter(writer));
            }

            final java.lang.Object run;
            switch (language) {
                case JAVASCRIPT:
                    run = js.eval(script);
                    break;
                case PYTHON:
                    run = python.eval(script);
                    break;
                case GROOVY:
                default:
                    run = groovy.eval(script);
                    break;
            }
            result.append(writer.toString()).append(System.lineSeparator());
            if (run != null) {
                result.append("return = ").append(run.toString());
            }
            writer.close();
        } catch (final CompilationFailedException | IOException | ScriptException e) {
            e.printStackTrace();
            result.append("Occurred Exception.").append(System.lineSeparator())
                .append(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.of(result.toString());
    }
}
