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

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.lang.Exception;

/** class processing messages for tone and translation */
public class NaturalLanguageProcessing {
  public static float testConfigurationNaturalLanguage() throws Exception{
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
        // Text to analyze
        String text = "depressed";
        Document doc = Document.newBuilder()
            .setContent(text).setType(Type.PLAIN_TEXT).build();

        // Detects the sentiment of the text
        Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

        return sentiment.getScore();
    }
  }

  public static String testConfigurationTranslation() throws Exception{
    // Instantiates a client
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    // Text to translate
    String text = "Sad";

    // Translates text into Spanish
    Translation translation =
        translate.translate(
            text,
            TranslateOption.sourceLanguage("en"),
            TranslateOption.targetLanguage("es"));

    return translation.getTranslatedText().toString();
  }
}
