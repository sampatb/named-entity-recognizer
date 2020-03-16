# চেনা অচেনা (Chena Awchena) - Named Entity Recognizer

## Intro
Named Entity Recognition (NER) is the task of recognition of nominal entities in a text. The system must identify and classify entities belonging to four categories: persons (PER), organizations (ORG), locations (LOC: cities, countries, etc) and miscellaneous (MISC).

## System description
For developing the system, following tools and resources have been used:
- WEKA: for training and testing
- Wikipedia: for collecting names of places, persons and organizations
- Yago2: for generating gazetteers and trigger words

#### Feature set used for NED
-	pos_tag			: The Part-of-Speech tag of w0
-	all_caps			: All the letters of w0 are capitalized
-	all_alpha			: All the letters of w0 are either upper or lower case
-	single_char			: w0 is a single character word
-	has_hyphen			: w0 contains a hyphen
-	has_quote			: w0 contains a quote
-	is_number			: w0 is a number
-	has_digit			: w0 contains a digit
-	is_punctuation		: w0 is a punctuation character
-	is_roman			: w0 is a roman numeral
-	is_functional_word		: w0 is a functional word
-	wL3_initCap			: w-3 starts with a capital letter
-	wL2_initCap			: w-2 starts with a capital letter
-	wL1_initCap			: w-1 starts with a capital letter
-	w0_initCap			: w0 starts with a capital letter
-	wR1_initCap			: w+1 starts with a capital letter
-	wR2_initCap			: w+2 starts with a capital letter
-	wR3_initCap			: w+3 starts with a capital letter
-	wL1_capitalized		: All letters of w-1 are capitalized
-	wR1_capitalized		: All letters of w-1 are capitalized
-	w0_capitalized_wL1_not	: All letters of w0 are capitalized but not w-1
-	w0_capitalized_wR1_not	: All letters of w0 are capitalized but not w+1
-	w0_wL1_capitalized		: All letters of w-1 and w0 are capitalized
-	wL1_w0_wR1_capitalized	: All letters of w-1 , w0 and w+1 are capitalized
-	w0_wR1_capitalized		: All letters of w0 and w+1 are capitalized
-	wL1_wR1_capitalized_w0_not  : All letters of w-1 and w+1 are capitalized but not w0


#### Feature set used for NEC
-	w-3, w-2, w-1, w0, w+1, w+2, w+3		: Word-bag [-3 to +3]
-	wL1_LOC_Trigger			: w-1 is a location trigger word
-	wR1_LOC_Trigger			: w+1 is a location trigger word
-	wL1_PER_Trigger			: w-1 is a person trigger word
-	wR1_PER_Trigger			: w+1 is a person trigger word
-	wL1_ORG_Trigger			: w-1 is a organization trigger word
-	wR1_ORG_Trigger			: w+1 is a organization trigger word
-	w0_in_location_gazetteer		: w0 is in location gazetteer
-	w0_in_person_gazetteer		: w0 is in person gazetteer
-	w0_in_organization_gazetteer	: w0 is in organization gazetteer
-	w0_in_misc_gazetteer		: w0 is in miscellaneous gazetteer



#### Three machine learning classifiers are used by the system
- J48 Classifier: Decision tree
- Naive Bayes Classifier
- kNN – IB3 Classifier
Among these, the decision tree classifier (J48) has been given more weight over others


## Performance on development set
After training with eng.train.bio data, the results obtained by testing the eng.development.bio are as below:

```
Type    Precision Recall  FB1
Overall 68.13     68.38   68.25
LOC     79.84     70.93   75.12
MISC    63.04     73.43   67.84
ORG     51.30     51.53   51.41
PER     72.84     75.57   74.18
```
       
## Conclusion
Feature selection is the most important part of NER task. During the task of prediction, errors are propagated from one module to another (BIO to Classifier) which drags the system performance down. Ambiguity is the bottleneck of the system. Most importantly, gazetteers are very useful in the task of classification (thanks to DbPedia and Yago).
