package com.voc2048.vocchipet.interaction

import com.voc2048.vocchipet.api.Tier
import com.voc2048.vocchipet.api.Element
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PetCommandParserTest {

    @Test
    fun testParseParameters() {
        val paramStr = "level=50 IT_HP=UR AT_ATK=25 shinny element=WATER"
        val params = PetAdminParser.parseParameters(paramStr)
        
        assertEquals("50", params["level"])
        assertEquals("UR", params["it_hp"])
        assertEquals("25", params["at_atk"])
        assertEquals("true", params["shinny"])
        assertEquals("WATER", params["element"])
    }

    @Test
    fun testCaseInsensitiveAndFlags() {
        val paramStr = "SHINNY level=10"
        val params = PetAdminParser.parseParameters(paramStr)
        
        assertEquals("true", params["shinny"])
        assertEquals("10", params["level"])
    }

    @Test
    fun testEmptyAndBlank() {
        val params = PetAdminParser.parseParameters("   ")
        assertTrue(params.isEmpty())
    }
}
