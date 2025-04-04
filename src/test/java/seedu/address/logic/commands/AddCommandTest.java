package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateNumber_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        Person validPersonNewName = new PersonBuilder().withName("New Person").build(); // still has same default number
        AddCommand addCommand = new AddCommand(validPersonNewName);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PHONE, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_personWithConflictingRecurringSchedule_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Person existingPerson = new PersonBuilder()
                .withName("Existing Person")
                .withPhone("91234567")
                .withRecurringSchedules("Monday 1400 1600")
                .build();
        modelStub.addPerson(existingPerson);

        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withRecurringSchedules("Monday 1500 1700")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(2, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
    }

    @Test
    public void execute_personWithConflictingOneTimeSchedule_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Person existingPerson = new PersonBuilder()
                .withName("Existing Person")
                .withPhone("91234567")
                .withOneTimeSchedules("31/03 1000 1200")
                .build();
        modelStub.addPerson(existingPerson);

        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withOneTimeSchedules("31/03 1100 1300")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(2, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
    }

    @Test
    public void execute_personWithConflictingRecurringAndOneTimeSchedule_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        Person existingPerson = new PersonBuilder()
                .withName("Existing Person")
                .withPhone("91234567")
                .withRecurringSchedules("Monday 1000 1200")
                .build();
        modelStub.addPerson(existingPerson);

        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withOneTimeSchedules("31/03 1100 1300")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);

        assertEquals(2, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));

        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
    }

    @Test
    public void execute_personWithInternalRecurringScheduleConflict_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        // Create a person with conflicting recurring schedules (same day, overlapping times)
        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withRecurringSchedules("Monday 1000 1200", "Monday 1100 1300")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(1, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
        assertTrue(commandResult.getFeedbackToUser().contains("Internal recurring schedule conflict"));
        assertTrue(commandResult.getFeedbackToUser().contains("same client"));
    }

    @Test
    public void execute_personWithInternalOneTimeScheduleConflict_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        // Create a person with conflicting one-time schedules (same date, overlapping times)
        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withOneTimeSchedules("15/04/25 1000 1200", "15/04/25 1100 1300")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(1, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
        assertTrue(commandResult.getFeedbackToUser().contains("Internal one-time schedule conflict"));
        assertTrue(commandResult.getFeedbackToUser().contains("same client"));
    }

    @Test
    public void execute_personWithInternalRecurringAndOneTimeConflict_addsPersonWithWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        // Create a person with a recurring schedule that conflicts with a one-time schedule
        // Assuming 21/04/25 is a Monday
        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withRecurringSchedules("Monday 1000 1200")
                .withOneTimeSchedules("21/04/25 1100 1300")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(1, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
        assertTrue(commandResult.getFeedbackToUser()
                .contains("Internal schedule conflict between recurring and one-time schedule"));
        assertTrue(commandResult.getFeedbackToUser().contains("same client"));
    }

    @Test
    public void execute_personWithBothInternalAndExternalConflicts_addsPersonWithAllWarnings() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();

        // Add an existing person with a schedule
        Person existingPerson = new PersonBuilder()
                .withName("Existing Person")
                .withPhone("91234567")
                .withRecurringSchedules("Monday 1400 1600")
                .build();
        modelStub.addPerson(existingPerson);

        // Create a new person with both internal conflicts and conflicts with the existing person
        Person newPerson = new PersonBuilder()
                .withName("New Person")
                .withPhone("87654321")
                .withRecurringSchedules("Monday 1000 1200", "Monday 1100 1300", "Monday 1500 1700")
                .build();

        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);
        assertEquals(2, modelStub.personsAdded.size());
        assertTrue(modelStub.personsAdded.contains(newPerson));
        assertTrue(commandResult.getFeedbackToUser().contains("schedule conflicts"));
        // Should contain both internal and external conflict messages
        assertTrue(commandResult.getFeedbackToUser().contains("Internal recurring schedule conflict"));
        assertTrue(commandResult.getFeedbackToUser().contains("Recurring schedule conflict on MONDAY between"));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPhone(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }

        @Override
        public boolean hasPhone(Person person) {
            requireNonNull(person);
            return this.person.hasSamePhone(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public boolean hasPhone(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::hasSamePhone);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            // Create an address book with the current list of persons
            AddressBook addressBook = new AddressBook();
            for (Person person : personsAdded) {
                addressBook.addPerson(person);
            }
            return addressBook;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(personsAdded);
        }
    }

}
