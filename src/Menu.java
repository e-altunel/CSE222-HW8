import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Menu {
	public static void
	main(final String[] args) {
		System.out.println("Welcome to the Social Network!");
		final Menu menu = new Menu();
		menu.StartSignalHandler();
		menu.menuLoop();
	}

	private static void
	printMenu() {
		System.out.println("1. Add a person");
		System.out.println("2. Remove a person");
		System.out.println("3. Add a friendship");
		System.out.println("4. Remove a friendship");
		System.out.println("5. Find the shortest path between two people");
		System.out.println("6. Suggestions");
		System.out.println("7. Clusters");
		System.out.println("8. Exit");
	}
	private static int
	readOption() {
		System.out.print("Enter an option: ");
		try {
			return Integer.parseInt(System.console().readLine());
		} catch (final NumberFormatException e) {
			return -1;
		}
	}

	private static Long
	parseTimestamp() {
		final String timestamp = System.console().readLine();
		try {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			final LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
			return localDateTime.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(localDateTime));
		} catch (final java.time.format.DateTimeParseException e) {
			System.out.println("Invalid timestamp");
			return null;
		}
	}

	private boolean signalCame = false;

	private final SocialNetwork socialNetwork = new SocialNetwork();

	private void
	menuLoop() {
		int option = 0;
		while (option != 8 && !signalCame) {
			printMenu();
			option = readOption();
			switch (option) {
			case 1:
				addPerson();
				break;
			case 2:
				removePerson();
				break;
			case 3:
				addFriendship();
				break;
			case 4:
				removeFriendship();
				break;
			case 5:
				findShortestPath();
				break;
			case 6:
				suggestions();
				break;
			case 7:
				clusters();
				break;
			case 8:
				System.out.println("Goodbye!");
				break;
			default:
				System.out.println("\n\033[31mError : \033[0mInvalid option\033[33m " + option +
						   "\033[0m\n");
			}
		}
		if (signalCame)
			System.out.println("I wish you had said goodbye...");
	}

	private void
	StartSignalHandler() {
		Signal.handle(new Signal("INT"), new SignalHandler() {
			public void handle(final Signal sig) {
				signalCame = true;
			}
		});
	}

	private void
	addPerson() {
		System.out.print("Enter the name of the person: ");
		final String name = System.console().readLine();
		System.out.print("Enter the age of the person: ");
		int age = 0;
		try {
			age = Integer.parseInt(System.console().readLine());
		} catch (final NumberFormatException e) {
			System.out.println("Invalid age");
			return;
		}
		System.out.print("Enter the hobbies of the person: ");
		final String hobbies = System.console().readLine();
		final Person person = new Person(name, age, hobbies);
		if (socialNetwork.addPerson(person))
			System.out.println("Person added successfully!" + person);
		else
			System.out.println("Person already exists!");
	}

	private void
	removePerson() {
		System.out.print("Enter the name of the person: ");
		final String name = System.console().readLine();
		if (socialNetwork.removePerson(name))
			System.out.println("Person removed successfully!");
		else
			System.out.println("Person does not exist!");
	}

	private void
	addFriendship() {
		System.out.print("Enter the name of the first person: ");
		final String name1 = System.console().readLine();
		System.out.print("Enter the timestamp of the first person: ");
		final Long timestamp1 = parseTimestamp();
		if (timestamp1 == null)
			return;
		System.out.print("Enter the name of the second person: ");
		final String name2 = System.console().readLine();
		System.out.print("Enter the timestamp of the second person: ");
		final Long timestamp2 = parseTimestamp();
		if (timestamp2 == null)
			return;
		if (socialNetwork.addFriendship(name1, timestamp1, name2, timestamp2))
			System.out.println("Friendship added successfully!");
		else
			System.out.println("Friendship cannot be added!");
	}

	private void
	removeFriendship() {
		System.out.print("Enter the name of the first person: ");
		final String name1 = System.console().readLine();
		System.out.print("Enter the timestamp of the first person: ");
		final Long timestamp1 = parseTimestamp();
		if (timestamp1 == null)
			return;
		System.out.print("Enter the name of the second person: ");
		final String name2 = System.console().readLine();
		System.out.print("Enter the timestamp of the second person: ");
		final Long timestamp2 = parseTimestamp();
		if (timestamp2 == null)
			return;
		if (socialNetwork.removeFriendship(name1, timestamp1, name2, timestamp2))
			System.out.println("Friendship removed successfully!");
		else
			System.out.println("Friendship does not exist!");
	}

	private void
	findShortestPath() {
		System.out.print("Enter the name of the first person: ");
		final String name1 = System.console().readLine();
		System.out.print("Enter the timestamp of the first person: ");
		final Long timestamp1 = parseTimestamp();
		if (timestamp1 == null)
			return;
		System.out.print("Enter the name of the second person: ");
		final String name2 = System.console().readLine();
		System.out.print("Enter the timestamp of the second person: ");
		final Long timestamp2 = parseTimestamp();
		if (timestamp2 == null)
			return;
		final List<Person> path = socialNetwork.shortestPath(name1, timestamp1, name2, timestamp2);
		if (path == null)
			System.out.println("No path found");
		else {
			System.out.println("Shortest path:");
			for (final Person person : path)
				System.out.println(person.getName());
		}
	}

	private void
	suggestions() {
		System.out.print("Enter the name of the person: ");
		final String name = System.console().readLine();
		System.out.print("Enter the timestamp of the person: ");
		final Long timestamp = parseTimestamp();
		if (timestamp == null)
			return;
		System.out.print("Suggestions count: ");
		int limit = 0;
		try {
			limit = Integer.parseInt(System.console().readLine());
		} catch (final NumberFormatException e) {
			System.out.println("Invalid limit");
			return;
		}
		socialNetwork.suggestFriends(name, timestamp, limit);
	}

	private void
	clusters() {
		socialNetwork.clusters();
	}
}