jUnit-os tesztek felsorolása
============================

Theses esetén:
--------------

- testCreateTheses_succesful()
- testCreateTheses_invalidSemesterId()
- testCreateTheses_invalidStudentId()
- testCreateTheses_invalidSupervisorId()
- testCreateTheses_missingTitle()
- testCreateTheses_missingFaculty()
- testCreateTheses_missingDepartment()
- testCreateTheses_missingSpeciality()
- testCreateTheses_missingLanguage()
- testCreateTheses_invalidThesisPdfId()
- testCreateTheses_invalidSupplementId()
- testCreateTheses_defenseScore()
- testCreateTheses_subjectScore()
- testCreateTheses_finalScore()

- testGetThesesById_successful()
- testGetThesesById_invalidId()

- testUpdateTheses_succesful()
- testUpdateTheses_invalidId()
- testUpdateTheses_invalidSemesterId()
- testUpdateTheses_invalidStudentId()
- testUpdateTheses_invalidSupervisorId()
- testUpdateTheses_missingTitle()
- testUpdateTheses_missingFaculty()
- testUpdateTheses_missingDepartment()
- testUpdateTheses_missingSpeciality()
- testUpdateTheses_missingLanguage()
- testUpdateTheses_invalidThesisPdfId()
- testUpdateTheses_invalidSupplementId()
- testUpdateTheses_defenseScore()
- testUpdateTheses_subjectScore()
- testUpdateTheses_finalScore()

- testRemoveTheses_succesful()
- testRemoveTheses_invalidId()

Users esetén:
-------------

- testCreateUsers_succesful()
- testCreateUsers_missingEmail()
- testCreateUsers_missingPassword()
- testCreateUsers_invalidTitle()
- testCreateUsers_missingFirstName()
- testCreateUsers_missingFamilyName()

- testGetUsersById_successful()
- testGetUsersById_invalidId()
- testRegister_Successful()
- testRegister_MissingData()
- testRegister_ExistingEmail()
- testRegister_WeakPassword()

- testUpdateUsers_succesful ()
- testUpdateUsers_invalidId()
- testUpdateUsers_missingEmail()
- testUpdateUsers_missingPassword()
- testUpdateUsers_invalidTitle()
- testUpdateUsers_missingFirstName()
- testUpdateUsers_missingFamilyName()

- testRemoveUsers_succesful()
- testRemoveUsers_invalidId()

User_roles esetén:
------------------

- testCreateUserRoles_succesful()
- testCreateUserRoles_missingRole()

- test_getUserRolesById_successful()
- test_getUserRolesById_invalidId()

- testUpdateUserRoles_succesful()
- test_updateUserRoles_invalidId()
- testUpdateUserRoles_missingRole()

- testRemoveUserRoles_succesful()
- testRemoveUserRoles_invalidId()

Titles esetén:
--------------

- testCreateTitles_succesful()
- testCreateTitles_missingName()

- testGetTitlesById_successful()
- testGetTitlesById_invalidId()

- testUpdateTitles_succesful()
- testUpdateTitles_invalidId()
- testUpdateTitles_missingName()

- testRemoveTitles_succesful()
- testRemoveTitles_invalidId()

Reviews esetén:
---------------

- testCreateReviews_succesful()
- testCreateReviews_invalidReviewerId()
- testCreateReviews_invalidThesisId()
- testCreateReviews_invalidScore()
- testCreateReviews_missingDescription()
- testCreateReviews_missingCity()

- testGetReviewsById_successful()
- testGetReviewsById_invalidId()

- testUpdateReviews_succesful()
- testUpdateReviews_invalidId()
- testUpdateReviews_invalidReviewerId()
- testUpdateReviews_invalidThesisId()
- testUpdateReviews_invalidScore()
- testUpdateReviews_missingDescription()
- testUpdateReviews_missingCity()

- testRemoveReviews_succesful()
- testRemoveReviews_invalidId()

Semesters esetén:
-----------------

- testCreateSemesters_succesful()
- testCreateSemesters_invalidYear()
- testCreateSemesters_invalidMonth()
- testCreateSemesters_missingLocation()

- testGetSemestersById_successful()
- testGetSemestersById_invalidId()

- testUpdateSemesters_succesful()
- testUpdateSemesters_invalidId
- testUpdateSemesters_invalidYear()
- testUpdateSemesters_invalidMonth()
- testUpdateSemesters_missingLocation()

- testRemoveSemesters_succesful()
- testRemoveSemesters_invalidId()

Files esetén:
-------------

- testCreateFiles_succesful()
- testCreateFiles_missingName()

- testGetFilesById_succesful()
- testGetFilesById_invalidId()

- testUpdateFiles_succesful()
- testUpdateFiles_invalidId()
- testUpdateFiles_missingName()

- testRemoveFiles_succesful()
- testRemoveFiles_invalidId()