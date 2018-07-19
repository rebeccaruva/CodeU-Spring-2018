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

package codeu;

import java.util.HashMap;
import codeu.natural_language.NaturalLanguageProcessing;

/** Maps language to language symbol. */
public class LanguageDictionary {
  private HashMap<String, String> languageDict;
  public LanguageDictionary(){
    languageDict = new HashMap<String, String>();
    languageDict.put("en", "English");
    //languageDict.put("af", "Afrikaans");
    //languageDict.put("sq", "Albanian");
    //languageDict.put("am", "Amharic");
    //languageDict.put("ar", "Arabic");
    //languageDict.put("hy", "Armenian");
    //languageDict.put("az", "Azerbaijani");
    //languageDict.put("eu", "Basque");
    //languageDict.put("be", "Belarusian");
    //languageDict.put("bn", "Bengali");
    //languageDict.put("bs", "Bosnian");
    //languageDict.put("bg", "Bulgarian");
    //languageDict.put("ca", "Catalan");
    //languageDict.put("ceb", "Cebuano");
    //languageDict.put("ny", "Chichewa");
    languageDict.put("zh", "Chinese(Simplified)");
    //languageDict.put("zh-TW", "Chinese(Traditional)");
    //languageDict.put("co", "Corsican");
    //languageDict.put("hr", "Croatian");
    //languageDict.put("cs", "Czech");
    //languageDict.put("da", "Danish");
    //languageDict.put("nl", "Dutch");
    //languageDict.put("eo", "Esperanto");
    //languageDict.put("et", "Estonian");
    //languageDict.put("tl", "Filipino");
    //languageDict.put("fi", "Finnish");
    languageDict.put("fr", "French");
    //languageDict.put("fy", "Frisian");
    //languageDict.put("gl", "Galician");
    //languageDict.put("ka", "Georgian");
    languageDict.put("de", "German");
    //languageDict.put("el", "Greek");
    //languageDict.put("gu", "Gujarati");
    //languageDict.put("ht", "Haitian Creole");
    //languageDict.put("ha", "Hausa");
    //languageDict.put("haw", "Hawaiian");
    //languageDict.put("iw", "Hebrew");
    languageDict.put("hi", "Hindi");
    //languageDict.put("hmn", "Hmong");
    //languageDict.put("hu", "Hungarian");
    //languageDict.put("is", "Icelandic");
    //languageDict.put("ig", "Igbo");
    //languageDict.put("id", "Indonesian");
    //languageDict.put("ga", "Irish");
    //languageDict.put("it", "Italian");
    languageDict.put("ja", "Japanese");
    //languageDict.put("jw", "Javanese");
    //languageDict.put("kn", "Kannada");
    //languageDict.put("kk", "Kazakh");
    //languageDict.put("km", "Khmer");
    languageDict.put("ko", "Korean");
    //languageDict.put("ku", "Kurdish(Kurmanji)");
    //languageDict.put("ky", "Kyrgyz");
    //languageDict.put("lo", "Lao");
    //languageDict.put("la", "Latin");
    //languageDict.put("lv", "Latvian");
    //languageDict.put("lt", "Lithuanian");
    //languageDict.put("lb", "Luxembourgish");
    //languageDict.put("mk", "Macedonian");
    //languageDict.put("mg", "Malagasy");
    //languageDict.put("ms", "Malay");
    //languageDict.put("ml", "Malayalam");
    //languageDict.put("mt", "Maltese");
    //languageDict.put("mi", "Maori");
    //languageDict.put("mr", "Marathi");
    //languageDict.put("mn", "Mongolian");
    //languageDict.put("my", "Myanmar(Burmese)");
    //languageDict.put("ne", "Nepali");
    //languageDict.put("no", "Norwegian");
    //languageDict.put("ps", "Pashto");
    //languageDict.put("fa", "Persian");
    //languageDict.put("pl", "Polish");
    //languageDict.put("pt", "Portuguese");
    //languageDict.put("pa", "Punjabi");
    //languageDict.put("ro", "Romanian");
    languageDict.put("ru", "Russian");
    //languageDict.put("sm", "Samoan");
    //languageDict.put("gd", "Scots Gaelic");
    //languageDict.put("sr", "Serbian");
    //languageDict.put("st", "Sesotho");
    //languageDict.put("sn", "Shona");
    //languageDict.put("sd", "Sindhi");
    //languageDict.put("si", "Sinhala");
    //languageDict.put("sk", "Slovok");
    //languageDict.put("sl", "Slovenian");
    //languageDict.put("so", "Somali");
    languageDict.put("es", "Spanish");
    //languageDict.put("su", "Sundanese");
    //languageDict.put("sw", "Swahili");
    //languageDict.put("sv", "Swedish");
    //languageDict.put("tg", "Tajik");
    //languageDict.put("ta", "Tamil");
    //languageDict.put("te", "Telegu");
    //languageDict.put("th", "Thai");
    //languageDict.put("tr", "Turkish");
    //languageDict.put("uk", "Ukranian");
    //languageDict.put("ur", "Urdu");
    //languageDict.put("uz", "Uzbek");
    //languageDict.put("vi", "Vietnamese");
    //languageDict.put("cy", "Welsh");
    //languageDict.put("xh", "Xhosa");
    //languageDict.put("yi", "Yiddish");
    //languageDict.put("yo", "Yoruba");
    //languageDict.put("zu", "Zulu");
  }

  /** gets value from dictionary */
  public String getValue(String key){
    return languageDict.get(key);
  }

  /** returns dictionary */
  public HashMap<String,String> getDict(){
    return languageDict;
  }
}
