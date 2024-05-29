import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialNetwork {
	private Map<String, Person> people;
	private Map<Person, List<Person> > friends;

	public SocialNetwork() {
		people = new HashMap<>();
		friends = new HashMap<>();
	}

	// Person management
	public boolean
	addPerson(final Person person) {
		if (people.containsKey(person.getName()))
			return false;

		people.put(person.getName(), person);
		return true;
	}

	public boolean
	removePerson(final String name) {
		if (!people.containsKey(name))
			return false;

		removeAllFriendships(people.get(name));
		people.remove(name);
		friends.remove(people.get(name));
		return true;
	}

	public Person
	getPerson(final String name) {
		return people.get(name);
	}

	// Friendship management
	public boolean
	addFriendship(final Person person1, final Person person2) {
		if (!people.containsValue(person1))
			return false;
		if (!people.containsValue(person2))
			return false;

		if (!friends.containsKey(person1))
			friends.put(person1, new ArrayList<>());
		if (!friends.containsKey(person2))
			friends.put(person2, new ArrayList<>());

		if (friends.get(person1).contains(person2) || friends.get(person2).contains(person1))
			return false;

		friends.get(person1).add(person2);
		friends.get(person2).add(person1);
		return true;
	}

	public boolean
	addFriendship(final String name1, final long timestamp1, final String name2, final long timestamp2) {
		if (!people.containsKey(name1) || !people.containsKey(name2)) {
			System.out.println("One or both people do not exist!");
			return false;
		}

		Person person1 = people.get(name1);
		Person person2 = people.get(name2);

		if (person1.getTimestamp() != timestamp1 || person2.getTimestamp() != timestamp2) {
			System.out.println("One or both timestamps are incorrect!");
			System.out.println("Person 1: " + person1.getTimestamp());
			System.out.println("Person 2: " + person2.getTimestamp());
			System.out.println("Timestamp 1: " + timestamp1);
			System.out.println("Timestamp 2: " + timestamp2);
			return false;
		}

		return addFriendship(person1, person2);
	}

	public boolean
	removeFriendship(final Person person1, final Person person2) {
		if (!people.containsValue(person1))
			return false;
		if (!people.containsValue(person2))
			return false;

		if (!friends.containsKey(person1))
			return false;
		if (!friends.containsKey(person2))
			return false;

		if (!friends.get(person1).contains(person2) || !friends.get(person2).contains(person1))
			return false;

		friends.get(person1).remove(person2);
		friends.get(person2).remove(person1);
		return true;
	}

	public boolean
	removeFriendship(final String name1, final long timestamp1, final String name2, final long timestamp2) {
		if (!people.containsKey(name1))
			return false;
		if (!people.containsKey(name2))
			return false;

		Person person1 = people.get(name1);
		Person person2 = people.get(name2);

		if (person1.getTimestamp() != timestamp1)
			return false;
		if (person2.getTimestamp() != timestamp2)
			return false;

		return removeFriendship(person1, person2);
	}

	public void
	removeAllFriendships(final Person person) {
		if (!friends.containsKey(person))
			return;
		for (Person friend : friends.get(person))
			friends.get(friend).remove(person);
		friends.remove(person);
	}

	public List<Person>
	getFriends(final Person person) {
		return friends.get(person);
	}

	private List<Person>
	shortestPath(final Person goal, final List<Person> previous) {
		if (goal == null || previous == null || previous.isEmpty())
			return null;
		Person current = previous.get(previous.size() - 1);
		if (current == goal)
			return previous;

		List<Person> shortest = null;
		for (Person friend : friends.get(current)) {
			if (previous.contains(friend))
				continue;

			List<Person> newPrevious = new ArrayList<>(previous);
			newPrevious.add(friend);
			List<Person> newPath = shortestPath(goal, newPrevious);
			if (newPath == null)
				continue;

			if (shortest == null || newPath.size() < shortest.size())
				shortest = newPath;
		}

		return shortest;
	}

	private List<Person>
	shortestPath(final Person person1, final Person person2) {
		if (person1 == null || person2 == null || person1 == person2 || !people.containsValue(person1) ||
		    !people.containsValue(person2) || !friends.containsKey(person1) || !friends.containsKey(person2))
			return null;

		List<Person> previous = new ArrayList<>();
		previous.add(person1);
		return shortestPath(person2, previous);
	}

	public List<Person>
	shortestPath(final String name1, final long timestamp1, final String name2, final long timestamp2) {
		if (!people.containsKey(name1))
			return null;
		if (!people.containsKey(name2))
			return null;

		Person person1 = people.get(name1);
		Person person2 = people.get(name2);

		if (person1.getTimestamp() != timestamp1)
			return null;
		if (person2.getTimestamp() != timestamp2)
			return null;

		return shortestPath(person1, person2);
	}

	private double
	calculateScore(final Person person1, final Person person2) {
		if (!people.containsValue(person1))
			return 0;
		if (!people.containsValue(person2))
			return 0;
		if (person1 == person2)
			return 0;
		if (friends.get(person1) != null && friends.get(person1).contains(person2))
			return 0;
		if (friends.get(person2) != null && friends.get(person2).contains(person1))
			return 0;

		double score = 0;
		score += mutualFriends(person1, person2);
		score += mutualHobbies(person1, person2) * 0.5;
		return score;
	}

	private int
	mutualFriends(final Person person1, final Person person2) {
		if (!friends.containsKey(person1))
			return 0;
		if (!friends.containsKey(person2))
			return 0;
		if (person1 == person2)
			return 0;

		int count = 0;
		for (Person friend : friends.get(person1))
			if (friends.get(person2).contains(friend))
				count++;
		return count;
	}

	private int
	mutualHobbies(final Person person1, final Person person2) {
		int count = 0;
		for (String hobby : person1.getHobbies())
			if (person2.getHobbies().contains(hobby))
				count++;
		return count;
	}

	private void
	suggestFriends(final Person person, final int limit) {
		if (!people.containsValue(person))
			return;

		Map<Person, Double> scores = new HashMap<>();
		for (Person friend : people.values())
			scores.put(friend, calculateScore(person, friend));

		Map<Person, Double> suggested = new HashMap<>();
		for (int i = 0; i < limit; i++) {
			Person best = null;
			double bestScore = 0;
			for (Person friend : scores.keySet()) {
				if (suggested.containsKey(friend))
					continue;
				if (best == null || scores.get(friend) > bestScore) {
					best = friend;
					bestScore = scores.get(friend);
				}
			}
			if (best == null)
				break;
			if (bestScore == 0)
				break;
			suggested.put(best, bestScore);
			System.out.println(best.getName() + " (" + bestScore +
					   ", Mutual Friends: " + mutualFriends(person, best) +
					   ", Mutual Hobbies: " + mutualHobbies(person, best) + ")");
		}
	}

	public void
	suggestFriends(final String name, final long timestamp, final int limit) {
		if (!people.containsKey(name))
			return;

		Person person = people.get(name);
		if (person.getTimestamp() != timestamp)
			return;

		suggestFriends(person, limit);
	}

	private List<Person>
	cluster(final Person person, final List<Person> previous) {
		if (person == null || previous == null || previous.isEmpty())
			return null;

		if (!friends.containsKey(person))
			return previous;
		List<Person> cluster = new ArrayList<>();
		cluster.add(person);
		for (Person friend : friends.get(person)) {
			if (previous.contains(friend))
				continue;

			List<Person> newPrevious = new ArrayList<>(previous);
			newPrevious.add(friend);
			List<Person> newCluster = cluster(friend, newPrevious);
			if (newCluster == null)
				continue;

			cluster.addAll(newCluster);
		}

		return cluster;
	}

	public void
	clusters() {
		List<List<Person> > clusters = new ArrayList<>();

		for (Person person : people.values()) {
			boolean found = false;
			for (List<Person> cluster : clusters) {
				if (cluster.contains(person)) {
					found = true;
					break;
				}
			}
			if (found)
				continue;

			List<Person> previous = new ArrayList<>();
			previous.add(person);
			List<Person> cluster = cluster(person, previous);
			if (cluster == null)
				continue;

			clusters.add(cluster);
		}

		for (List<Person> cluster : clusters) {
			System.out.println("Cluster:");
			for (Person person : cluster)
				System.out.println(person.getName());
			System.out.println();
		}
		if (clusters.isEmpty())
			System.out.println("No clusters found");
	}
}
