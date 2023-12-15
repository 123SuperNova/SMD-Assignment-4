package com.example.assignment4.sampledata

import java.io.Serializable

/** A Plus Code encoded location reference.  */
class PlusCode : Serializable {
    /** The global Plus Code identifier.  */
    var globalCode: String? = null

    /** The compound Plus Code identifier. May be null for locations in remote areas.  */
    var compoundCode: String? = null

    override fun toString(): String {
        val sb = StringBuilder("[PlusCode: ")
        sb.append(globalCode)
        if (compoundCode != null) {
            sb.append(", compoundCode=").append(compoundCode)
        }
        sb.append("]")
        return sb.toString()
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}