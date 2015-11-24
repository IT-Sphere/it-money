package ru.itsphere.itmoney.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itsphere.itmoney.domain.User;

import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Реализация UserDAO для работы через текстовый файл
 * <p>
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Component
public class UserDAOTextFileImpl implements UserDAO {
    /**
     * Подключили логгер к текущему классу
     */
    private static final Logger logger = LogManager.getLogger(UserDAOTextFileImpl.class);

    public static final String SEPARATOR = "/";

    @Autowired
    private ReaderFactory readerFactory;
    @Autowired
    private WriterFactory writerFactory;

    @Override
    public User getById(int id) {
        try {
            String[] splittedLine = getSplittedLineById(id);
            if (splittedLine == null) {
                logger.warn("User with id '{}' was not found", id);
                return null;
            }
            return new User(getId(splittedLine), getName(splittedLine));
        } catch (Exception e) {
            throw new DAOException(String.format("Getting user by id %s", id), e);
        }
    }

    private String getName(String[] splittedLine) {
        return splittedLine[1];
    }

    private int getId(String[] splittedLine) {
        return Integer.parseInt(splittedLine[0]);
    }

    private String[] getSplittedLineById(int id) throws Exception {
        try (LineNumberReader reader = readerFactory.getLineNumberReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (reader.getLineNumber() == 1) {
                    continue;
                }
                String[] splittedLine = line.split(SEPARATOR);
                int lineId = getId(splittedLine);
                if (lineId == id) {
                    return splittedLine;
                }
            }
            return null;
        }
    }

    @Override
    public void save(User user) {
        try {
            String lastLine = getLastLine();
            if (isHeader(lastLine)) {
                writeUserToFileEndWithNewId(user, 0);
            } else {
                int lastUserId = getId(lastLine.split(SEPARATOR));
                writeUserToFileEndWithNewId(user, lastUserId + 1);
            }
        } catch (Exception e) {
            throw new DAOException("User isn't creating", e);
        }
    }

    private boolean isHeader(String line) throws Exception {
        String firstLine = getFirstLine();
        return line.equals(firstLine);
    }

    @Override
    public void update(User user) {
        try {
            List<String> lines = replaceWithNewData(user, getLinesOfFile());
            updateFile(lines);
        } catch (Exception e) {
            throw new DAOException("User isn't update", e);
        }
    }

    @Override
    public List<User> getAll() {
        try {
            List<User> result = new ArrayList<>();
            ListIterator<String> iterator = getLinesOfFile().listIterator(1);
            while (iterator.hasNext()) {
                result.add(convertToUser(iterator.next()));
            }
            return result;
        } catch (Exception e) {
            throw new DAOException("Users aren't geting", e);
        }
    }

    private User convertToUser(String userLine) {
        String[] splittedLine = userLine.split(SEPARATOR);
        return new User(getId(splittedLine), getName(splittedLine));
    }

    private void updateFile(List<String> lines) throws Exception {
        boolean append = false;
        try (PrintWriter writer = writerFactory.getPrintWriter(append)) {
            lines.forEach(line -> {
                writer.println(line);
            });
        }
    }

    private List<String> replaceWithNewData(User user, List<String> lines) {
        int userIndexInFile = getUserIndexInLines(user.getId(), lines);
        lines.remove(userIndexInFile);
        lines.add(userIndexInFile, createNewLineForFile(user));
        return lines;
    }

    private String createNewLineForFile(User user) {
        return user.getId() + SEPARATOR + user.getName();
    }

    private List<String> getLinesOfFile() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = readerFactory.getLineNumberReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private void writeUserToFileEndWithNewId(User user, int newId) throws Exception {
        User userForSaving = prepareUserForSaving(user, newId);
        writeUser(userForSaving);
    }


    private void writeUser(User userForSaving) throws Exception {
        boolean append = true;
        try (PrintWriter writer = writerFactory.getPrintWriter(append)) {
            writer.println(createNewLineForFile(userForSaving));
        }
    }

    private User prepareUserForSaving(User user, int lastUserId) {
        return new User(lastUserId, user.getName());
    }

    private String getLastLine() throws Exception {
        try (LineNumberReader reader = readerFactory.getLineNumberReader()) {
            String result = null;
            String line;
            while ((line = reader.readLine()) != null) {
                result = line;
            }
            return result;
        }
    }

    private String getFirstLine() throws Exception {
        try (LineNumberReader reader = readerFactory.getLineNumberReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                return line;
            }
            throw new RuntimeException("The store file is invalid");
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            List<String> lines = deleteUserFromList(id, getLinesOfFile());
            updateFile(lines);
        } catch (Exception e) {
            throw new DAOException(String.format("Deleting user by id %s", id), e);
        }
    }

    private List<String> deleteUserFromList(int id, List<String> lines) {
        int userIndexInFile = getUserIndexInLines(id, lines);
        lines.remove(userIndexInFile);
        return lines;
    }

    private int getUserIndexInLines(int id, List<String> lines) {
        ListIterator<String> iterator = lines.listIterator(1);
        while (iterator.hasNext()) {
            String line = iterator.next();
            String[] splittedLine = line.split(SEPARATOR);
            if (getId(splittedLine) == id) {
                return iterator.nextIndex() - 1;
            }
        }
        throw new RuntimeException("The user was not found");
    }

    public void setReaderFactory(ReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    public void setWriterFactory(WriterFactory writerFactory) {
        this.writerFactory = writerFactory;
    }
}
