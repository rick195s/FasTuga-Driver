package com.example.fastugadriver.ui.edit_profile

import com.example.fastugadriver.data.pojos.auth.LoggedInDriver

data class EditProfileResult(
    val success: LoggedInDriver? = null,
    val errors: EditProfileErrors? = null
)