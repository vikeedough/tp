package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DisplayCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DisplayCommand object
 */
public class DisplayCommandParser implements Parser<DisplayCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DisplayCommand
     * and returns a DisplayCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DisplayCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new DisplayCommand(index);
    }

}
