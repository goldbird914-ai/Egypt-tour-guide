package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.EgyptTourRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("أم الدنيا", appName)
  }

  @Test
  fun `verify egypt tour repository contains rich monuments and routes`() {
    val monuments = EgyptTourRepository.monuments
    assertTrue("Should have multiple historical monuments", monuments.size >= 5)

    val giza = EgyptTourRepository.getMonumentById("giza_pyramids")
    assertNotNull("Giza Pyramids should be present", giza)
    assertEquals("الجيزة", giza?.cityAr)

    val routes = EgyptTourRepository.tourRoutes
    assertTrue("Should have suggested routes for Egyptian regions", routes.size >= 4)

    val arArtifacts = EgyptTourRepository.arArtifacts
    assertTrue("Should have AR artifacts for virtual exploration", arArtifacts.size >= 3)
  }

  @Test
  fun `verify tts narrator initialization and speech rate controls`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val narrator = com.example.tts.TtsNarrator(context)
    narrator.setRate(0.85f)
    assertEquals(0.85f, narrator.speechRate, 0.01f)
    narrator.shutdown()
  }

  @Test
  fun `verify language switcher and strings dictionary`() {
    val arName = com.example.data.AppStrings.appName(com.example.data.AppLanguage.ARABIC)
    val enName = com.example.data.AppStrings.appName(com.example.data.AppLanguage.ENGLISH)
    val frName = com.example.data.AppStrings.appName(com.example.data.AppLanguage.FRENCH)
    val zhName = com.example.data.AppStrings.appName(com.example.data.AppLanguage.CHINESE)
    assertEquals("أم الدنيا", arName)
    assertEquals("Om El Donia", enName)
    assertEquals("Om El Donia", frName)
    assertEquals("埃及之母 (Om El Donia)", zhName)

    val giza = EgyptTourRepository.getMonumentById("giza_pyramids")
    assertNotNull(giza)
    assertEquals("أهرامات الجيزة الخالدة", giza?.name(com.example.data.AppLanguage.ARABIC))
    assertEquals("Great Pyramids of Giza", giza?.name(com.example.data.AppLanguage.ENGLISH))
    assertEquals("Plateau des Pyramides de Gizeh", giza?.name(com.example.data.AppLanguage.FRENCH))
    assertEquals("Pyramiden-Plateau von Gizeh", giza?.name(com.example.data.AppLanguage.GERMAN))
    assertEquals("吉萨金字塔高原与皇家陵寝", giza?.name(com.example.data.AppLanguage.CHINESE))
    assertEquals("ギザの三大ピラミッド高原", giza?.name(com.example.data.AppLanguage.JAPANESE))
  }
}
