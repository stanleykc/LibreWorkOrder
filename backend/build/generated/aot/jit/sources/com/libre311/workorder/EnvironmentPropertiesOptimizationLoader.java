package com.libre311.workorder;

import io.micronaut.core.optim.StaticOptimizations;
import io.micronaut.core.util.EnvironmentProperties;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentPropertiesOptimizationLoader implements StaticOptimizations.Loader<EnvironmentProperties> {
  private void load0(Map<String, List<String>> env) {
    env.put("PATH", Arrays.asList("path"));
    env.put("OTEL_EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE", Arrays.asList("otel.exporter.otlp.metrics.temporality.preference", "otel.exporter.otlp.metrics.temporality-preference", "otel.exporter.otlp.metrics-temporality.preference", "otel.exporter.otlp.metrics-temporality-preference", "otel.exporter.otlp-metrics.temporality.preference", "otel.exporter.otlp-metrics.temporality-preference", "otel.exporter.otlp-metrics-temporality.preference", "otel.exporter.otlp-metrics-temporality-preference", "otel.exporter-otlp.metrics.temporality.preference", "otel.exporter-otlp.metrics.temporality-preference", "otel.exporter-otlp.metrics-temporality.preference", "otel.exporter-otlp.metrics-temporality-preference", "otel.exporter-otlp-metrics.temporality.preference", "otel.exporter-otlp-metrics.temporality-preference", "otel.exporter-otlp-metrics-temporality.preference", "otel.exporter-otlp-metrics-temporality-preference", "otel-exporter.otlp.metrics.temporality.preference", "otel-exporter.otlp.metrics.temporality-preference", "otel-exporter.otlp.metrics-temporality.preference", "otel-exporter.otlp.metrics-temporality-preference", "otel-exporter.otlp-metrics.temporality.preference", "otel-exporter.otlp-metrics.temporality-preference", "otel-exporter.otlp-metrics-temporality.preference", "otel-exporter.otlp-metrics-temporality-preference", "otel-exporter-otlp.metrics.temporality.preference", "otel-exporter-otlp.metrics.temporality-preference", "otel-exporter-otlp.metrics-temporality.preference", "otel-exporter-otlp.metrics-temporality-preference", "otel-exporter-otlp-metrics.temporality.preference", "otel-exporter-otlp-metrics.temporality-preference", "otel-exporter-otlp-metrics-temporality.preference", "otel-exporter-otlp-metrics-temporality-preference"));
    env.put("GIT_EDITOR", Arrays.asList("git.editor", "git-editor"));
    env.put("TERM", Arrays.asList("term"));
    env.put("LANG", Arrays.asList("lang"));
    env.put("HOMEBREW_PREFIX", Arrays.asList("homebrew.prefix", "homebrew-prefix"));
    env.put("COREPACK_ENABLE_AUTO_PIN", Arrays.asList("corepack.enable.auto.pin", "corepack.enable.auto-pin", "corepack.enable-auto.pin", "corepack.enable-auto-pin", "corepack-enable.auto.pin", "corepack-enable.auto-pin", "corepack-enable-auto.pin", "corepack-enable-auto-pin"));
    env.put("CLAUDE_CODE_ENTRYPOINT", Arrays.asList("claude.code.entrypoint", "claude.code-entrypoint", "claude-code.entrypoint", "claude-code-entrypoint"));
    env.put("NVM_INC", Arrays.asList("nvm.inc", "nvm-inc"));
    env.put("CLAUDECODE", Arrays.asList("claudecode"));
    env.put("LOGNAME", Arrays.asList("logname"));
    env.put("HOMEBREW_REPOSITORY", Arrays.asList("homebrew.repository", "homebrew-repository"));
    env.put("PWD", Arrays.asList("pwd"));
    env.put("XPC_SERVICE_NAME", Arrays.asList("xpc.service.name", "xpc.service-name", "xpc-service.name", "xpc-service-name"));
    env.put("TERM_PROGRAM_VERSION", Arrays.asList("term.program.version", "term.program-version", "term-program.version", "term-program-version"));
    env.put("INFOPATH", Arrays.asList("infopath"));
    env.put("__CFBundleIdentifier", Arrays.asList("..cfbundleidentifier", ".-cfbundleidentifier", "-.cfbundleidentifier", "--cfbundleidentifier"));
    env.put("NVM_CD_FLAGS", Arrays.asList("nvm.cd.flags", "nvm.cd-flags", "nvm-cd.flags", "nvm-cd-flags"));
    env.put("NVM_DIR", Arrays.asList("nvm.dir", "nvm-dir"));
    env.put("SHELL", Arrays.asList("shell"));
    env.put("TERM_PROGRAM", Arrays.asList("term.program", "term-program"));
    env.put("HOMEBREW_CELLAR", Arrays.asList("homebrew.cellar", "homebrew-cellar"));
    env.put("USER", Arrays.asList("user"));
    env.put("TMPDIR", Arrays.asList("tmpdir"));
    env.put("SSH_AUTH_SOCK", Arrays.asList("ssh.auth.sock", "ssh.auth-sock", "ssh-auth.sock", "ssh-auth-sock"));
    env.put("VIRTUAL_ENV", Arrays.asList("virtual.env", "virtual-env"));
    env.put("XPC_FLAGS", Arrays.asList("xpc.flags", "xpc-flags"));
    env.put("TERM_SESSION_ID", Arrays.asList("term.session.id", "term.session-id", "term-session.id", "term-session-id"));
    env.put("__CF_USER_TEXT_ENCODING", Arrays.asList("..cf.user.text.encoding", "..cf.user.text-encoding", "..cf.user-text.encoding", "..cf.user-text-encoding", "..cf-user.text.encoding", "..cf-user.text-encoding", "..cf-user-text.encoding", "..cf-user-text-encoding", ".-cf.user.text.encoding", ".-cf.user.text-encoding", ".-cf.user-text.encoding", ".-cf.user-text-encoding", ".-cf-user.text.encoding", ".-cf-user.text-encoding", ".-cf-user-text.encoding", ".-cf-user-text-encoding", "-.cf.user.text.encoding", "-.cf.user.text-encoding", "-.cf.user-text.encoding", "-.cf.user-text-encoding", "-.cf-user.text.encoding", "-.cf-user.text-encoding", "-.cf-user-text.encoding", "-.cf-user-text-encoding", "--cf.user.text.encoding", "--cf.user.text-encoding", "--cf.user-text.encoding", "--cf.user-text-encoding", "--cf-user.text.encoding", "--cf-user.text-encoding", "--cf-user-text.encoding", "--cf-user-text-encoding"));
    env.put("NVM_BIN", Arrays.asList("nvm.bin", "nvm-bin"));
    env.put("VIRTUAL_ENV_PROMPT", Arrays.asList("virtual.env.prompt", "virtual.env-prompt", "virtual-env.prompt", "virtual-env-prompt"));
    env.put("HOME", Arrays.asList("home"));
    env.put("SHLVL", Arrays.asList("shlvl"));
  }

  @Override
  public EnvironmentProperties load() {
    Map<String, List<String>> env = new HashMap<String, List<String>>();
    load0(env);
    return EnvironmentProperties.of(env);
  }
}
