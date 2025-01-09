plugins {
    alias(libs.plugins.algomate)
    alias(libs.plugins.style)
}

version = file("version").readLines().first()

exercise {
    assignmentId.set("h05")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    // BEISPIEL:
    // studentId = "ab12cdef"
    // firstName = "sol_first"
    // lastName = "sol_last"
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
}

jagr {
    graders {
        val graderPublic by getting
        val graderPrivate by creating {
            parent(graderPublic)
            graderName.set("FOP-2425-H05-Private")
            rubricProviderName.set("h05.H05_RubricProvider")
        }
    }
}
