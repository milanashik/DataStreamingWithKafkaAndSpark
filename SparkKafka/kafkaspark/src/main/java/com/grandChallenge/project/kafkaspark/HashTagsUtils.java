package com.grandChallenge.project.kafkaspark;

import java.util.*;
import java.util.regex.*;

public class HashTagsUtils {
	 private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

	    public static Iterator<String> hashTagsFromTweet(String text) {
	        List<String> hashTags = new ArrayList<>();
	        Matcher matcher = HASHTAG_PATTERN.matcher(text);
	        while (matcher.find()) {
	            String handle = matcher.group();
	            hashTags.add(handle);
	        }
	        return hashTags.iterator();
	    }
}
