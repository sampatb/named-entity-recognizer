package ner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Named Entity Recognizer (NER) that performs recognition of nominal entities in given text.
 */
public class NamedEntityRecognizer {

    private final HashSet<String> _location_triggers = new HashSet<String>();
    private final HashSet<String> _person_triggers = new HashSet<String>();
    private final HashSet<String> _org_triggers = new HashSet<String>();
    private final HashSet<String> _special_symbols = new HashSet<String>();
    private final HashSet<String> _functional_word = new HashSet<String>();
    private final HashSet<String> _location_gazetteer = new HashSet<String>();
    private final HashSet<String> _organization_gazetteer = new HashSet<String>();
    private final HashSet<String> _person_gazetteer = new HashSet<String>();
    private final String trigger_files[] = { "location_triggers.txt", "person_triggers.txt", "organization_triggers.txt" };
    private final String gazetteer_files[] = { "location_gazetteer.txt", "person_gazetteer.txt", "organization_gazetteer.txt" };
    private final String functional_word_file = "functional_wordlist.txt";

    /*
     * Recognizes nominal entities in given text.
     */
    public void recognize(final String filename) {

        // Input file
        final File file = new File(filename);

        FileInputStream f;
        DataInputStream d;
        BufferedReader b;
        String trigger_word;

        try {
            // Populate Location trigger words
            f = new FileInputStream(trigger_files[0]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _location_triggers.add(trigger_word);
            }

            // Populate Person trigger words
            f = new FileInputStream(trigger_files[1]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _person_triggers.add(trigger_word);
            }

            // Populate Organization trigger words
            f = new FileInputStream(trigger_files[2]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _org_triggers.add(trigger_word);
            }

            // Populate Location gazetteer
            f = new FileInputStream(gazetteer_files[0]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _location_gazetteer.add(trigger_word);
            }

            // Populate Person gazetteer
            f = new FileInputStream(gazetteer_files[1]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _person_gazetteer.add(trigger_word);
            }

            // Populate Organization gazetteer
            f = new FileInputStream(gazetteer_files[2]);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _organization_gazetteer.add(trigger_word);
            }

            _special_symbols.add(",");
            _special_symbols.add(".");
            _special_symbols.add("-");
            _special_symbols.add("\"");
            _special_symbols.add("(");
            _special_symbols.add(")");
            _special_symbols.add("--");
            _special_symbols.add(":");
            _special_symbols.add("-X-");
            _special_symbols.add("$");
            _special_symbols.add("''");

            // Populate functional word list gazetteer
            f = new FileInputStream(functional_word_file);
            d = new DataInputStream(f);
            b = new BufferedReader(new InputStreamReader(d));

            while ((trigger_word = b.readLine()) != null) {
                _functional_word.add(trigger_word);
            }

            // Remove the empty string from all hash sets
            _location_triggers.remove("");
            _person_triggers.remove("");
            _org_triggers.remove("");
            _special_symbols.remove("");
            _functional_word.remove("");
            _location_gazetteer.remove("");
            _organization_gazetteer.remove("");
            _person_gazetteer.remove("");

            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader brAhead = new BufferedReader(new InputStreamReader(dis));

            BufferedWriter out = new BufferedWriter(new FileWriter("ner-bio-test-data.arff"));

            String newLineChar = System.getProperty("line.separator");

            out.write("@relation english_named_entity" + newLineChar);

            out.write(newLineChar);

            out.write("@attribute pos_tag { ");
            out.write("WRB, ");    // Wh-adverb however whenever where why
            out.write("WP$, ");    // WH-pronoun, possessive whose
            out.write("WP, ");        // WH-pronoun that what whatever which who whom
            out.write("WDT, ");    // WH-determiner that what whatever which whichever
            out.write("VBZ, ");        // verb, present tense, 3rd person singular bases reconstructs marks uses
            out.write("VBP, ");    // verb, present tense, not 3rd person singular twist appear comprise mold postpone
            out.write("VBN, ");        // verb, past participle dilapidated imitated reunifed unsettled
            out.write("VBG, ");        // verb, present participle or gerund stirring focusing approaching erasing
            out.write("VBD, ");    // verb, past tense pleaded swiped registered saw
            out.write("VB, ");        // verb, base form ask bring fire see take
            out.write("UH, ");        // interjection huh howdy uh whammo shucks heck
            out.write("TO, ");        // "to" as preposition or infinitive marker to
            out.write("RP, ");        // particle aboard away back by on open through
            out.write("RBS, ");        // adverb, superlative best biggest nearest worst
            out.write("RBR, ");        // adverb, comparative further gloomier heavier less-perfectly
            out.write("RB, ");        // adverb occasionally maddeningly adventurously
            out.write("PRP$, ");    // pronoun, possessive her his mine my our ours their thy your
            out.write("PRP, ");        // pronoun, personal hers himself it we them
            out.write("POS, ");        // genitive marker ' 's
            out.write("NNS, ");        // noun, common, plural undergraduates bric-a-brac averages
            out.write("NNPS, ");    // noun, proper, plural Americans Materials States
            out.write("NNP, ");        // noun, proper, singular Motown Cougar Yvette Liverpool
            out.write("NN, ");        // noun, common, singular or mass cabbage thermostat investment subhumanity
            out.write("MD, ");        // modal auxiliary can may might will would
            out.write("JJS, ");        // adjective, superlative bravest cheapest tallest
            out.write("JJR, ");        // adjective, comparative braver cheaper taller
            out.write("JJ, ");        // adjective or numeral, ordinal third ill-mannered regrettable
            out.write("IN, ");        // preposition or conjunction, subordinating among whether out on by if
            out.write("FW, ");        // foreign word gemeinschaft hund ich jeux
            out.write("EX, ");        // existential there there
            out.write("DT, ");        // determiner a all an every no that the
            out.write("CD, ");        // numeral, cardinal mid-1890 nine-thirty 0.5 one
            out.write("CC, ");        // conjunction, coordinating and both but either or
            out.write("PDT, ");
            out.write("LS, ");
            out.write("NN|SYM, ");
            out.write("EOF, ");
            out.write("SYM, ");        // Symbols / * 
            out.write("SYMB} ");    // Symbols , . - " ( ) -- \ -X- : $
            out.write(newLineChar);

            // more attribtes or features

            out.write("@attribute all_caps numeric" + newLineChar);
            out.write("@attribute all_alpha numeric" + newLineChar);
            out.write("@attribute single_char numeric" + newLineChar);
            out.write("@attribute has_hyphen numeric" + newLineChar);
            out.write("@attribute has_quote numeric" + newLineChar);
            out.write("@attribute is_number numeric" + newLineChar);
            out.write("@attribute has_digit numeric" + newLineChar);
            out.write("@attribute is_punctuation numeric" + newLineChar);
            out.write("@attribute is_roman numeric" + newLineChar);
            out.write("@attribute is_functional_word numeric" + newLineChar);

            out.write("@attribute wL3_initCap numeric" + newLineChar);
            out.write("@attribute wL2_initCap numeric" + newLineChar);
            out.write("@attribute wL1_initCap numeric" + newLineChar);
            out.write("@attribute w0_initCap numeric" + newLineChar);
            out.write("@attribute wR1_initCap numeric" + newLineChar);
            out.write("@attribute wR2_initCap numeric" + newLineChar);
            out.write("@attribute wR3_initCap numeric" + newLineChar);

            out.write("@attribute wL1_capitalized numeric" + newLineChar);
            out.write("@attribute wR1_capitalized numeric" + newLineChar);
            out.write("@attribute w0_capitalized_wL1_not numeric" + newLineChar);
            out.write("@attribute w0_capitalized_wR1_not numeric" + newLineChar);
            out.write("@attribute w0_wL1_capitalized numeric" + newLineChar);
            out.write("@attribute wL1_w0_wR1_capitalized numeric" + newLineChar);
            out.write("@attribute w0_wR1_capitalized numeric" + newLineChar);
            out.write("@attribute wL1_wR1_capitalized_w0_not numeric" + newLineChar);

            //out.write("@attribute w0_position numeric"+newLineChar);
            //out.write("@attribute w0_frequency numeric"+newLineChar);

                /*
                out.write("@attribute wL3_wordBag string"+newLineChar);
                out.write("@attribute wL2_wordBag string"+newLineChar);
                out.write("@attribute wL1_wordBag string"+newLineChar);
                out.write("@attribute w0_wordBag string"+newLineChar);
                out.write("@attribute wR1_wordBag string"+newLineChar);
                out.write("@attribute wR2_wordBag string"+newLineChar);
                out.write("@attribute wR3_wordBag string"+newLineChar);
                */

            //out.write("@attribute entity_first_word numeric"+newLineChar);
            //out.write("@attribute entity_second_word numeric"+newLineChar);

                /*
                out.write("@attribute wL1_LOC_Trigger numeric"+newLineChar);
                out.write("@attribute wR1_LOC_Trigger numeric"+newLineChar);

                out.write("@attribute wL1_PER_Trigger numeric"+newLineChar);
                out.write("@attribute wR1_PER_Trigger numeric"+newLineChar);

                out.write("@attribute wL1_ORG_Trigger numeric"+newLineChar);
                out.write("@attribute wR1_ORG_Trigger numeric"+newLineChar);
                */

            //out.write("@attribute w0_in_location_gazetteer numeric"+newLineChar);
            //out.write("@attribute w0_in_person_gazetteer numeric"+newLineChar);
            //out.write("@attribute w0_in_organization_gazetteer numeric"+newLineChar);

            //out.write("@attribute class { B-PER, B-LOC, B-ORG, B-MISC, I-PER, I-LOC, I-ORG, I-MISC, O}" + newLineChar);
            out.write("@attribute class { B, I, O}" + newLineChar);

            out.write(newLineChar);

            out.write("@data" + newLineChar);

            String currentWord;
            String windowBuffer[] = new String[7];
            String buffer[] = new String[7];
            boolean flag = false;
            int count = 3;

            // Initialize the initCapBuffer
            windowBuffer[0] = new String();
            windowBuffer[1] = new String();
            windowBuffer[2] = new String();
            windowBuffer[3] = new String();
            buffer[0] = new String();
            buffer[1] = new String();
            buffer[2] = new String();
            buffer[3] = new String();
            for (int i = 4; i < 7; i++) {
                String str = brAhead.readLine();
                buffer[i] = str;
                String s[] = str.split(" ");
                if (s.length > 0) {
                    windowBuffer[i] = s[0];
                } else {
                    windowBuffer[i] = new String();
                }
            }

            while (count != 0) {

                // Rotate the windowBuffer-----------
                //////////////////////////////////////
                for (int i = 0; i < 6; i++) {
                    windowBuffer[i] = windowBuffer[i + 1];
                    buffer[i] = buffer[i + 1];
                }

                // Populate the last element of windowBuffer
                String str;
                if (!flag && ((str = brAhead.readLine()) != null)) {
                    buffer[6] = str;
                    String s[] = str.split(" ");
                    if (s.length > 0) {
                        windowBuffer[6] = s[0];
                    } else {
                        windowBuffer[6] = new String();
                    }
                } else {
                    buffer[6] = new String();
                    windowBuffer[6] = new String();
                    flag = true;
                }

                currentWord = buffer[3];

                // Tokenize the current input line
                String tokens[] = currentWord.split(" ");

                if (tokens.length > 1) {

                    // Populate the feature vector

                    // pos_tag
                    if (!_special_symbols.contains(tokens[1])) {
                        out.write(tokens[1]);
                    } else {
                        out.write("SYMB");
                    }
                    out.write(",");

                    // all_caps
                    if (tokens[0].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // all_alpha
                    if (tokens[0].matches("[A-Za-z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // single_char
                    if (tokens[0].matches("[A-Za-z]")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // has_hyphen
                    if (tokens[0].matches(".*-.*")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // has_quote
                    if (tokens[0].matches(".*'.*")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // is_number
                    if (tokens[0].matches("[0-9,]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // has_digit
                    if (tokens[0].matches(".*[0-9].*")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // is_punctuation
                    if (tokens[0].matches("[`~!@#$%^&*()-=_+\\[\\]\\\\{}|;\':\\\",./<>?]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // is_roman
                    if (tokens[0].matches("[IVXDLCM]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // is_functional_word
                    if (_functional_word.contains(tokens[0])) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // wL3_initCap, wL2_initCap, wL1_initCap, 
                    // w0_initCap,
                    // wR1_initCap, wR2_initCap, wR3_initCap
                    for (int i = 0; i < 7; i++) {
                        if (windowBuffer[i].matches("[A-Z].*")) {
                            out.write("1");
                        } else {
                            out.write("0");
                        }
                        out.write(",");
                    }

                    //----------------------------------------------------------
                    // wL1_capitalized
                    if (windowBuffer[2].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // wR1_capitalized
                    if (windowBuffer[4].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // w0_capitalized_wL1_not 
                    if (tokens[0].matches("[A-Z]+") && !windowBuffer[2].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // w0_capitalized_wR1_not 
                    if (tokens[0].matches("[A-Z]+") && !windowBuffer[4].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // w0_wL1_capitalized 
                    if (tokens[0].matches("[A-Z]+") && windowBuffer[2].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // wL1_w0_wR1_capitalized
                    if (windowBuffer[2].matches("[A-Z]+") && tokens[0].matches("[A-Z]+") && windowBuffer[4].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // w0_wR1_capitalized
                    if (tokens[0].matches("[A-Z]+") && windowBuffer[4].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    // wL1_wR1_capitalized_w0_not 
                    if (windowBuffer[2].matches("[A-Z]+") && !tokens[0].matches("[A-Z]+") && windowBuffer[4].matches("[A-Z]+")) {
                        out.write("1");
                    } else {
                        out.write("0");
                    }
                    out.write(",");

                    //----------------------------------------------------------

                                /*
                                for(int i=0; i<7; i++) {
                                        if(windowBuffer[i].matches("[A-Za-z]+")) 
                                                out.write(windowBuffer[i]);
                                        else
                                                out.write("SYMB");
                                        out.write(",");
                                }
                                */
                                /*
                                // wL1_LOC_Trigger, wR1_LOC_Trigger
                                if(_location_triggers.contains(windowBuffer[2]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");
                                if(_location_triggers.contains(windowBuffer[4]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");


                                // wL1_PER_Trigger, wR1_PER_Trigger
                                if(_person_triggers.contains(windowBuffer[2]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");
                                if(_person_triggers.contains(windowBuffer[4]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");


                                // wL1_ORG_Trigger, wR1_ORG_Trigger
                                if(_org_triggers.contains(windowBuffer[2]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");
                                if(_org_triggers.contains(windowBuffer[4]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");



                                // w0_in_location_gazetteer
                                if(_location_gazetteer.contains(tokens[0]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");
                                */
                                /*
                                // w0_in_person_gazetteer
                                if(_person_gazetteer.contains(tokens[0]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");


                                // w0_in_organization_gazetteer
                                if(_organization_gazetteer.contains(tokens[0]))
                                        out.write("1");
                                else
                                        out.write("0");
                                out.write(",");
                                */

                    // class
                    out.write("?");
                    out.write(newLineChar);
                } else {
                    out.write(newLineChar);
                }

                if (flag) {
                    count--;
                }

            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

