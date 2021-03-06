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

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.IOException;

/** class processing messages for tone and translation */
public class NaturalLanguageProcessing {
  /** check tone of text */
  public static String checkTone(String text) throws IOException {
    LanguageServiceClient language = LanguageServiceClient.create();
    Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

    // Detects the sentiment of the text
    Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();

    // Returns whether professional (score > -0.4) or not
    if (score > -0.3) return "Sounds professional!";
    else {
      return "Please consider re-phrasing. This may not come across as professional.";
    }
  }

  /** translate text from sourceLanguage to targetLanguage */
  public static String translate(String text, String sourceLanguage, String targetLanguage)
      throws IOException {
    if (sourceLanguage.equals(targetLanguage)) return text;
    // Instantiates a client
    Translate translate = TranslateOptions.getDefaultInstance().getService();

    // Translates text into target language
    Translation translation =
        translate.translate(
            text,
            TranslateOption.sourceLanguage(sourceLanguage),
            TranslateOption.targetLanguage(targetLanguage));

    return translation.getTranslatedText().toString();
  }
}
