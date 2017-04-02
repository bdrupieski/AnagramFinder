package anagramutils.persistence;

import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;
import anagramutils.AnagramMatch;

import java.lang.annotation.*;

@BindingAnnotation(BindAnagramMatch.SomethingBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindAnagramMatch {
    class SomethingBinderFactory implements BinderFactory {
        public Binder build(Annotation annotation) {
            return (Binder<BindAnagramMatch, AnagramMatch>) (q, bind, arg) -> {
                q.bind("id", arg.getId());
                q.bind("tweet1Id", arg.getTweet1Id());
                q.bind("tweet2Id", arg.getTweet2Id());
                q.bind("editDistanceOriginalText", arg.getEditDistanceOriginalText());
                q.bind("editDistanceStrippedText", arg.getEditDistanceStrippedText());
                q.bind("hammingDistanceStrippedText", arg.getHammingDistanceStrippedText());
                q.bind("longestCommonSubstringLengthStrippedText", arg.getLongestCommonSubstringLengthStrippedText());
                q.bind("wordCountDifference", arg.getWordCountDifference());
                q.bind("totalUniqueWords", arg.getTotalUniqueWords());
                q.bind("lcsLengthToTotalLengthRatio", arg.getLcsLengthToTotalLengthRatio());
                q.bind("editDistanceToLengthRatio", arg.getEditDistanceToLengthRatio());
                q.bind("differentWordCountToTotalWordCount", arg.getDifferentWordCountToTotalWordCount());
                q.bind("englishWordsToTotalWordCount", arg.getEnglishWordsToTotalWordCount());
                q.bind("totalLengthToHighestLengthCapturedRatio", arg.getTotalLengthToHighestLengthCapturedRatio());
                q.bind("isSameRearranged", arg.getIsSameRearranged().getDatabaseValue());
                q.bind("interestingFactor", arg.getInterestingFactor());
            };
        }
    }
}
