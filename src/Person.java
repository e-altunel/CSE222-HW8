import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Person {
	private String name;
	private Integer age;
	private List<String> hobbies;
	private Long timestamp;

	public Person(final String name, final Integer age, final String hobbies) {
		this.name = name;
		this.age = age;
		this.hobbies = Arrays.asList(hobbies.split(","));
		this.timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
	}

	public String
	getName() {
		return name;
	}

	public void
	setName(final String name) {
		this.name = name;
	}

	public Integer
	getAge() {
		return age;
	}

	public void
	setAge(final Integer age) {
		this.age = age;
	}

	public List<String>
	getHobbies() {
		return hobbies;
	}

	public void
	setHobbies(final List<String> hobbies) {
		this.hobbies = hobbies;
	}

	public Long
	getTimestamp() {
		return timestamp;
	}

	public void
	setTimestamp(final Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String
	toString() {
		return getName() + " (" +
			LocalDateTime.ofInstant(Instant.ofEpochSecond(getTimestamp()), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
			")";
	}
}