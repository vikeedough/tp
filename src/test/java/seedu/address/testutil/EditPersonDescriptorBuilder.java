package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.OneTimeSchedule;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setRecurringSchedules(person.getRecurringSchedules());
        descriptor.setGoals(person.getGoals());
        descriptor.setLocation(person.getLocation());
        descriptor.setOneTimeSchedules(person.getOneTimeSchedules());
        descriptor.setMedicalHistory(person.getMedicalHistory());
        descriptor.setTags(person.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Parses the {@code recurringSchedules} into a {@code Set<RecurringSchedule>} and set it
     * to the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRecurringSchedules(String... recurringSchedules) {
        Set<RecurringSchedule> recurringScheduleSet = Stream.of(recurringSchedules).map(RecurringSchedule::new)
                .collect(Collectors.toSet());
        descriptor.setRecurringSchedules(recurringScheduleSet);
        return this;
    }

    /**
     * Sets the {@code Medical History} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withMedicalHistory(String medicalHistory) {
        descriptor.setMedicalHistory(new MedicalHistory(medicalHistory));
        return this;
    }

    /**
     * Sets the {@code Goals} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGoals(String goals) {
        descriptor.setGoals(new Goals(goals));
        return this;
    }

    /**
     * Sets the {@code Location} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withLocation(String location) {
        descriptor.setLocation(new Location(location));
        return this;
    }

    /**
     * Sets the {@code OneTimeSchedule} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withOneTimeSchedules(String... oneTimeSchedules) {
        Set<OneTimeSchedule> oneTimeScheduleSet = Stream.of(oneTimeSchedules)
                .map(OneTimeSchedule::new)
                .collect(Collectors.toSet());
        descriptor.setOneTimeSchedules(oneTimeScheduleSet);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }

}
