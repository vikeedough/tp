package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.GOALS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GOALS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GOALS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LOCATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MEDICAL_HISTORY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RECURRING_SCHEDULE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.MEDICAL_HISTORY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.MEDICAL_HISTORY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.ONETIMESCHEDULE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ONETIMESCHEDULE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.RECURRING_SCHEDULE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.RECURRING_SCHEDULE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GOALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEDICAL_HISTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Goals;
import seedu.address.model.person.Location;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RecurringSchedule;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB
                        + RECURRING_SCHEDULE_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB
                        + LOCATION_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                        + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + LOCATION_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + LOCATION_DESC_BOB
                + ONETIMESCHEDULE_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple medical histories
        assertParseFailure(parser, MEDICAL_HISTORY_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEDICAL_HISTORY));

        // multiple addresses
        assertParseFailure(parser, LOCATION_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + NAME_DESC_AMY + LOCATION_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_LOCATION, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid goals
        assertParseFailure(parser, INVALID_GOALS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GOALS));

        // invalid medical history
        assertParseFailure(parser, INVALID_MEDICAL_HISTORY_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEDICAL_HISTORY));

        // invalid address
        assertParseFailure(parser, INVALID_LOCATION_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid goals
        assertParseFailure(parser, validExpectedPersonString + INVALID_GOALS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GOALS));

        // invalid medical history
        assertParseFailure(parser, validExpectedPersonString + INVALID_MEDICAL_HISTORY_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEDICAL_HISTORY));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_LOCATION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LOCATION));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + RECURRING_SCHEDULE_DESC_AMY
                        + GOALS_DESC_AMY + MEDICAL_HISTORY_DESC_AMY + LOCATION_DESC_AMY + ONETIMESCHEDULE_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + LOCATION_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + LOCATION_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_LOCATION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + LOCATION_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + RECURRING_SCHEDULE_DESC_BOB
                + LOCATION_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid recurring schedule
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_RECURRING_SCHEDULE_DESC
                + LOCATION_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                RecurringSchedule.MESSAGE_CONSTRAINTS);

        // invalid goals
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + LOCATION_DESC_BOB + INVALID_GOALS_DESC + MEDICAL_HISTORY_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Goals.MESSAGE_CONSTRAINTS);

        // invalid medical history
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + LOCATION_DESC_BOB + GOALS_DESC_BOB + INVALID_MEDICAL_HISTORY_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                MedicalHistory.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + INVALID_LOCATION_DESC + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Location.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + LOCATION_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + INVALID_LOCATION_DESC, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + RECURRING_SCHEDULE_DESC_BOB
                + PHONE_DESC_BOB + GOALS_DESC_BOB + MEDICAL_HISTORY_DESC_BOB + LOCATION_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
