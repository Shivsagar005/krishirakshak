package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KrishiRakshak AI", appName)
  }

  @Test
  fun `verify multi factor risk calculation`() {
    val result = com.example.data.MockAndDemoRepository.submitScan(
      cropName = "Tomato",
      cropHindiName = "टमाटर",
      detectedIssue = "Early Blight",
      issueHindi = "अगेती झुलसा",
      category = com.example.model.ScanCategory.DISEASE,
      confidence = 0.89f,
      severity = com.example.model.SeverityLevel.SEVERE,
      symptoms = listOf("Concentric rings")
    )
    org.junit.Assert.assertTrue(result.riskScore > 50)
    org.junit.Assert.assertEquals(com.example.model.RiskLevel.CRITICAL, result.riskLevel)
  }
}
