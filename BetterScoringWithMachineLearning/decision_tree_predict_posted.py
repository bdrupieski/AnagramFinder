from anagram_match import get_matches_from_csv
from sklearn import tree
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix, classification_report, matthews_corrcoef
from confusion_matrix_util import plot_confusion_matrix
import matplotlib.pyplot as plt


def build_training_and_test_data():

    matches = get_matches_from_csv()

    X = [[match.english_words_to_total_word_count_ratio,
          match.different_word_count_to_total_word_count_ratio,
          match.edit_distance_to_length_ratio,
          match.inverse_lcs_length_to_total_length_ratio,
          match.total_length_to_highest_length_captured_ratio,
          match.interesting_factor] for match in matches]

    y = [1 if match.posted is True else 0 for match in matches]

    return X, y


def predict_posted_decision_tree():
    X, Y = build_training_and_test_data()
    X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=0.20, random_state=42)

    clf = tree.DecisionTreeClassifier()
    clf.fit(X_train, y_train)

    y_true, y_pred = y_test, clf.predict(X_test)
    print("matthews correlation co-efficient: {0}".format(matthews_corrcoef(y_true, y_pred)))
    print(classification_report(y_true, y_pred))
    plt.figure()
    plot_confusion_matrix(confusion_matrix(y_test, y_pred), ["rejected", "posted"], title="decision tree")
    plt.show()


if __name__ == "__main__":
    predict_posted_decision_tree()
