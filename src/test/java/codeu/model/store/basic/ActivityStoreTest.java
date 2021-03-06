package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityStoreTest {

  private ActivityStore activityStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Activity ACTIVITY_ONE =
      new Activity(
          Activity.Type.LOGGED_IN,
          UUID.randomUUID(),
          Instant.ofEpochMilli(1000),
          UUID.randomUUID());

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    activityStore = ActivityStore.getTestInstance(mockPersistentStorageAgent);

    final List<Activity> activityList = new ArrayList<>();
    activityList.add(ACTIVITY_ONE);
    activityStore.setActivities(activityList);
  }

  @Test
  public void testAddActivity() {
    Activity inputActivity =
        new Activity(
            Activity.Type.NEW_CONVERSATION, UUID.randomUUID(), Instant.now(), UUID.randomUUID());
    activityStore.addActivity(inputActivity);

    Activity resultActivity = activityStore.getAllActivities().get(1);

    assertEquivalentActivities(inputActivity, resultActivity);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputActivity);
  }

  private void assertEquivalentActivities(Activity expectedActivity, Activity actualActivity) {
    Assert.assertEquals(expectedActivity.getId(), actualActivity.getId());
    Assert.assertEquals(expectedActivity.getObjectId(), actualActivity.getObjectId());
    Assert.assertEquals(expectedActivity.getCreationTime(), actualActivity.getCreationTime());
  }
}
