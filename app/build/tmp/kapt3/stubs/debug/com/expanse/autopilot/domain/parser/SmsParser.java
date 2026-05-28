package com.expanse.autopilot.domain.parser;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006\u00c0\u0006\u0003"}, d2 = {"Lcom/expanse/autopilot/domain/parser/SmsParser;", "", "parse", "Lcom/expanse/autopilot/domain/parser/ParsedSms;", "smsBody", "", "app_debug"})
public abstract interface SmsParser {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.expanse.autopilot.domain.parser.ParsedSms parse(@org.jetbrains.annotations.NotNull()
    java.lang.String smsBody);
}