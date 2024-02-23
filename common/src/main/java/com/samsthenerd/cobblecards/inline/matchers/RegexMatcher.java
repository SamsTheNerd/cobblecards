package com.samsthenerd.cobblecards.inline.matchers;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.cobblecards.inline.InlineMatchResult;
import com.samsthenerd.cobblecards.inline.InlineMatcher;

import net.minecraft.util.Pair;

public interface RegexMatcher extends InlineMatcher {
    public default InlineMatchResult match(String input){
        Matcher regexMatcher = getRegex().matcher(input);
        InlineMatchResult result = new InlineMatchResult();
        while(regexMatcher.find()){
            MatchResult mr = regexMatcher.toMatchResult();
            Pair<InlineMatchResult.Match, Integer> matchAndGroup = getMatchAndGroup(mr);
            if(matchAndGroup.getLeft() == null) continue;
            result.addMatch(mr.start(matchAndGroup.getRight()), mr.end(matchAndGroup.getRight()), matchAndGroup.getLeft());
        }
        return result;
    }

    public Pattern getRegex();

    @NotNull
    public default Pair<InlineMatchResult.Match, Integer> getMatchAndGroup(MatchResult regexMatch){
        return new Pair<>(getMatch(regexMatch), 0);
    }

    @Nullable
    public InlineMatchResult.Match getMatch(MatchResult regexMatch);

    public static class Simple implements RegexMatcher {
        private Pattern regex;
        private Function<MatchResult, InlineMatchResult.Match> matcher;

        public Simple(Pattern regex, Function<MatchResult, InlineMatchResult.Match> matcher){
            this.regex = regex;
            this.matcher = matcher;
        }

        public Simple(String regex, Function<MatchResult, InlineMatchResult.Match> matcher){
            this(Pattern.compile(regex), matcher);
        }

        public Pattern getRegex(){
            return regex;
        }

        public InlineMatchResult.Match getMatch(MatchResult regexMatch){
            return matcher.apply(regexMatch);
        }
    }
}
