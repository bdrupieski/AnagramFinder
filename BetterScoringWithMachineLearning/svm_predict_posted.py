from anagram_match import get_matches_from_csv
from sklearn import svm
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.metrics import confusion_matrix, classification_report

matches = get_matches_from_csv()

X = [[match.english_words_to_total_word_count_ratio,
      match.different_word_count_to_total_word_count_ratio,
      match.edit_distance_to_length_ratio,
      match.inverse_lcs_length_to_total_length_ratio,
      match.total_length_to_highest_length_captured_ratio,
      match.interesting_factor] for match in matches]

y = [1 if match.posted is True else 0 for match in matches]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20, random_state=42)

search_parameters = [{'kernel': ['rbf'], 'gamma': [1e-3, 1e-4], 'C': [1, 10, 100, 1000]},
                     {'kernel': ['linear'], 'C': [1, 10, 100, 1000]}]

clf = GridSearchCV(svm.SVC(C=1), search_parameters, cv=5)
clf.fit(X_train, y_train)

print(clf.best_params_)
means = clf.cv_results_['mean_test_score']
stds = clf.cv_results_['std_test_score']
for mean, std, params in zip(means, stds, clf.cv_results_['params']):
    print("%0.3f (+/-%0.03f) for %r" % (mean, std * 2, params))

y_true, y_pred = y_test, clf.predict(X_test)
print(classification_report(y_true, y_pred))
print(confusion_matrix(y_test, y_pred).ravel())
