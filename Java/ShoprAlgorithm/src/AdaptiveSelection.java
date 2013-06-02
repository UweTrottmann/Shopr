public class AdaptiveSelection {

	public static void main(String[] args) {
		adaptiveSelection();
	}

	/**
	 * Runs one recommendation cycle.
	 */
	private static void adaptiveSelection() {
		itemRecommend();
		userReview();
		queryRevise();
	}

	/**
	 * Takes the case-base, current query, number of recommended items to return
	 * and returns a list of recommended items.
	 */
	private static void itemRecommend() {
		// TODO Auto-generated method stub

	}

	/**
	 * Takes the list of recommended items and elicits a critique on a feature
	 * of one item from the user. Returns the liked/disliked item and which
	 * feature value (! not just which feature !) was liked/disliked.
	 */
	private static void userReview() {
		// TODO Auto-generated method stub
	}

	/**
	 * Takes a liked/disliked item, which feature value was liked/disliked, the
	 * current query. Returns a new query modified according to the given user
	 * critique.
	 */
	private static void queryRevise() {
		// TODO Auto-generated method stub

	}

}
