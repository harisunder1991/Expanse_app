package com.expanse.autopilot.domain.parser;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\tH\u0016R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/expanse/autopilot/domain/parser/SmsParserImpl;", "Lcom/expanse/autopilot/domain/parser/SmsParser;", "<init>", "()V", "amountRegex", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "debitKeywords", "", "", "creditKeywords", "merchantKeywords", "parse", "Lcom/expanse/autopilot/domain/parser/ParsedSms;", "smsBody", "app_debug"})
public final class SmsParserImpl implements com.expanse.autopilot.domain.parser.SmsParser {
    private final java.util.regex.Pattern amountRegex = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> debitKeywords = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> creditKeywords = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> merchantKeywords = null;
    
    public SmsParserImpl() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.expanse.autopilot.domain.parser.ParsedSms parse(@org.jetbrains.annotations.NotNull()
    java.lang.String smsBody) {
        return null;
    }
}