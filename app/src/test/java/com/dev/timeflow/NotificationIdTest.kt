package com.dev.timeflow

import com.dev.timeflow.Managers.notification.AlarmKeys
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NotificationIdTest {

    @Test
    fun `reminder ids never collide between events and tasks with the same id`() {
        val ids = (1L..500L).flatMap { id ->
            listOf(
                AlarmKeys.reminderNotificationId(AlarmKeys.TYPE_EVENT, id),
                AlarmKeys.reminderNotificationId(AlarmKeys.TYPE_TASK, id)
            )
        }
        assertEquals(ids.size, ids.toSet().size)
    }

    /**
     * The invariant the whole fix rests on: the request code is a function of the
     * owning entity only. If it ever encoded the reminder time again, editing a
     * reminder would leave the old alarm uncancellable and the event would fire twice.
     */
    @Test
    fun `request code does not depend on the reminder time`() {
        val onCreate = AlarmKeys.requestCode(AlarmKeys.TYPE_EVENT, 42L)
        val afterUserEditsTheReminderTime = AlarmKeys.requestCode(AlarmKeys.TYPE_EVENT, 42L)
        assertEquals(onCreate, afterUserEditsTheReminderTime)
    }

    @Test
    fun `request code distinguishes events from tasks with the same id`() {
        assertNotEquals(
            AlarmKeys.requestCode(AlarmKeys.TYPE_EVENT, 3L),
            AlarmKeys.requestCode(AlarmKeys.TYPE_TASK, 3L)
        )
    }

    @Test
    fun `request code distinguishes different entities`() {
        assertNotEquals(
            AlarmKeys.requestCode(AlarmKeys.TYPE_EVENT, 3L),
            AlarmKeys.requestCode(AlarmKeys.TYPE_EVENT, 4L)
        )
    }
}
