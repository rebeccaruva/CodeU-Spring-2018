package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class NotificationTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID notifiedUser = UUID.randomUUID();
    UUID message = UUID.randomUUID();
    Instant creation = Instant.now();

    Notification notification = new Notification(id, notifiedUser, message, creation);

    Assert.assertEquals(id, notification.getId());
    Assert.assertEquals(notifiedUser, notification.getNotifiedUserUUID());
    Assert.assertEquals(message, notification.getMessageUUID());
    Assert.assertEquals(creation, notification.getCreationTime());
    Assert.assertEquals(false, notification.getViewedStatus());
  }
}
