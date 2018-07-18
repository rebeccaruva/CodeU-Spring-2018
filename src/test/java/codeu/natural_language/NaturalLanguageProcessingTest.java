// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.natural_language;

import codeu.configuration.JsonConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NaturalLanguageProcessingTest {

  private NaturalLanguageProcessing nlp = new NaturalLanguageProcessing();
  private Float returnedSentimentValue;
  private String returnedTranslatedString;
  private ArrayList<String> test_strings;

  /* create Json file with Google credentials for APIs */
  @Before
  public void makeJson() {
    (new JsonConfiguration()).createJson();
  }

  /* test configuration for Natural Language API */
  @Test
  public void testConfigurationNaturalLanguage() {
    returnedSentimentValue = new Float(0.0);
    test_strings = new ArrayList<String>();
    test_strings.add("");
    test_strings.add("a");
    test_strings.add("sad");
    test_strings.add("happy");
    test_strings.add("depressed");
    test_strings.add("mad");
    test_strings.add("furious");
    test_strings.add("I am not very happy.");
    test_strings.add("I am extremely depressed.");
    test_strings.add("This is trash.");
    test_strings.add("This is not good.");
    test_strings.add("I hate this so much.");
    test_strings.add("I dislike this.");

    ArrayList<Float> expectedValues = new ArrayList<Float>();
    expectedValues.add(new Float(0.0));
    expectedValues.add(new Float(0.0));
    expectedValues.add(new Float(-0.3));
    expectedValues.add(new Float(0.4));
    expectedValues.add(new Float(-0.4));
    expectedValues.add(new Float(0.0));
    expectedValues.add(new Float(0.1));
    expectedValues.add(new Float(-0.5));
    expectedValues.add(new Float(-0.6));
    expectedValues.add(new Float(-0.8));
    expectedValues.add(new Float(-0.6));
    expectedValues.add(new Float(-0.7));
    expectedValues.add(new Float(-0.7));

    try {
      for (int i = 0; i < test_strings.size(); i++) {
        returnedSentimentValue = new Float(nlp.configurationNaturalLanguage(test_strings.get(i)));
        Assert.assertEquals(expectedValues.get(i), returnedSentimentValue);
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  /* test configuration for Google Cloud Translate API */
  @Test
  public void testConfigurationTranslation() {
    returnedTranslatedString = "";
    test_strings = new ArrayList<String>();
    String originalString = "I think that this test is going to work.";

    try {
      ArrayList<String> test_strings = new ArrayList<String>();
      test_strings.add(nlp.configurationTranslation(originalString, "en", "ru"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "hi"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "es"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "ar"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "el"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "bg"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "zh"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "nl"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "fr"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "la"));
      test_strings.add(nlp.configurationTranslation(originalString, "en", "th"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }

    ArrayList<String> expectedValues = new ArrayList<String>();
    expectedValues.add("Я думаю, что этот тест будет работать."); // ru
    expectedValues.add("मुझे लगता है कि यह परीक्षण काम करने जा रहा है।"); // hi
    expectedValues.add("Creo que esta prueba va a funcionar."); // es
    expectedValues.add("أعتقد أن هذا الاختبار سيعمل."); // ar
    expectedValues.add("Νομίζω ότι αυτή η δοκιμασία θα λειτουργήσει."); // el
    expectedValues.add("Мисля, че този тест ще работи."); // bg
    expectedValues.add("我认为这个测试会起作用。"); // zh
    expectedValues.add("Ik denk dat deze test gaat werken."); // nl
    expectedValues.add("Je pense que ce test va marcher."); // fr
    expectedValues.add("Ego puto hoc esse ad operandum test."); // la
    expectedValues.add("ฉันคิดว่าการทดสอบนี้เป็นไปได้"); // th

    for (int i = 0; i < test_strings.size(); i++) {
      Assert.assertEquals(expectedValues.get(i), test_strings.get(i));
    }
  }
}
