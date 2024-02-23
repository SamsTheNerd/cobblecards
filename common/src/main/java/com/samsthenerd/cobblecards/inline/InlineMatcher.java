package com.samsthenerd.cobblecards.inline;

@FunctionalInterface
public interface InlineMatcher {
    public InlineMatchResult match(String input);
}
