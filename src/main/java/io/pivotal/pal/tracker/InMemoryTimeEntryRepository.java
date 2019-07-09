package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Long counter = 0L;

    private Map<Long, TimeEntry> cheesecakeFactory = new HashMap<Long, TimeEntry>();


    public InMemoryTimeEntryRepository() {
    }


    public TimeEntry create(TimeEntry timeEntry) {
        counter = counter + 1L;
        TimeEntry savedTimeEntry = new TimeEntry(counter, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        cheesecakeFactory.put(counter, savedTimeEntry);

        return savedTimeEntry;
    }

    public TimeEntry find(long id) {
        return cheesecakeFactory.get(id);
    }


    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (cheesecakeFactory.get(id) == null) {
            return null;
        }
        TimeEntry updatedEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        cheesecakeFactory.replace(id, updatedEntry);
        return updatedEntry;
    }

    public void delete(long id) {
        cheesecakeFactory.remove(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> listTimeEntry = new ArrayList<>();
        for(Map.Entry<Long, TimeEntry> entry: cheesecakeFactory.entrySet()) {
            listTimeEntry.add(entry.getValue());
        }
        return listTimeEntry;
    }
}
