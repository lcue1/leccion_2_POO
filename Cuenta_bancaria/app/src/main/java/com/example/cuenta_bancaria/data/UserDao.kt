    package com.example.cuenta_bancaria.data

    import android.content.ContentValues
    import android.database.Cursor

    class UserDao(private val dbHelper: DatabaseHelper) {

        companion object {
            const val TABLE_NAME = "users"
            const val COLUMN_ID = "id"
            const val COLUMN_NUMBERACOUNT = "numberAcount"
            const val COLUMN_NAME = "name"
            const val COLUMN_BALANCE = "balance"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PASSWORD = "password"
            const val COLUMN_TYPE_ACOUNT = "typeAcount"

            const val CREATE_TABLE = """
                CREATE TABLE $TABLE_NAME (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NAME TEXT,
                    $COLUMN_NUMBERACOUNT TEXT UNIQUE,
                    $COLUMN_BALANCE REAL,
                    $COLUMN_USERNAME TEXT,
                    $COLUMN_PASSWORD TEXT,
                    $COLUMN_TYPE_ACOUNT TEXT
                )
            """
        }

        fun insertUser(name: String, numberAcount: String, balance: Double, username: String, password: String, typeAcount: String): Long {
            var round=String.format("%.2f", balance).toDouble()
            round = round.toDouble()

            val db = dbHelper.writableDatabase
            return try {
                val values = ContentValues().apply {
                    put(COLUMN_NAME, name)
                    put(COLUMN_NUMBERACOUNT, numberAcount)
                    put(COLUMN_BALANCE, round)
                    put(COLUMN_USERNAME, username)
                    put(COLUMN_PASSWORD, password)
                    put(COLUMN_TYPE_ACOUNT, typeAcount)
                }
                db.insert(TABLE_NAME, null, values)
            } catch (e: Exception) {
                e.printStackTrace()
                -1L
            } finally {
                db.close()
            }
        }

        fun getAllUsers(): List<Map<String, Any>> {
            val db = dbHelper.readableDatabase
            val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            val users = mutableListOf<Map<String, Any>>()

            with(cursor) {
                while (moveToNext()) {
                    val user = mapOf(
                        COLUMN_ID to getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        COLUMN_NAME to getString(getColumnIndexOrThrow(COLUMN_NAME)),
                        COLUMN_NUMBERACOUNT to getString(getColumnIndexOrThrow(COLUMN_NUMBERACOUNT)),
                        COLUMN_BALANCE to getDouble(getColumnIndexOrThrow(COLUMN_BALANCE)),
                        COLUMN_USERNAME to getString(getColumnIndexOrThrow(COLUMN_USERNAME)),
                        COLUMN_TYPE_ACOUNT to getString(getColumnIndexOrThrow(COLUMN_TYPE_ACOUNT))
                    )
                    users.add(user)
                }
            }

            cursor.close()
            return users
        }


        fun getUserInformation(userAcount: String, password: String): List<String> {
            val db = dbHelper.readableDatabase
            val accountDetails = mutableListOf<String>()

            // Define the selection criteria
            val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
            val selectionArgs = arrayOf(userAcount, password)

            val cursor: Cursor = db.query(
                TABLE_NAME, // Table name
                null, // Columns (null means all columns)
                selection, // Selection (WHERE clause)
                selectionArgs, // Selection arguments
                null, // Group by
                null, // Having
                null // Order by
            )

            with(cursor) {
                if (moveToFirst()) {
                    accountDetails.add(getString(getColumnIndexOrThrow(COLUMN_NAME)))
                    accountDetails.add(getString(getColumnIndexOrThrow(COLUMN_NUMBERACOUNT)))
                    accountDetails.add(getDouble(getColumnIndexOrThrow(COLUMN_BALANCE)).toString())
                    accountDetails.add(getString(getColumnIndexOrThrow(COLUMN_USERNAME)))
                    accountDetails.add(getString(getColumnIndexOrThrow(COLUMN_TYPE_ACOUNT)))
                }
            }

            cursor.close()
            return accountDetails
        }

        //update deposit
        fun updateBalance(accountNumber: String, newBalance: Double): Int {
            var round=String.format("%.2f", newBalance).toDouble()
            round = round.toDouble()
            val db = dbHelper.writableDatabase

            // Create ContentValues object to hold the new balance
            val values = ContentValues().apply {
                put(COLUMN_BALANCE, round)
            }

            // Define the selection criteria (where clause)
            val selection = "$COLUMN_NUMBERACOUNT = ?"
            val selectionArgs = arrayOf(accountNumber)

            return try {
                // Perform the update operation and return the number of rows affected
                val rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs)
                rowsAffected // Returns the number of rows affected (should be 1 if successful)
            } catch (e: Exception) {
                e.printStackTrace()
                -1 // Return -1 if there's an error
            } finally {
                db.close() // Always close the database
            }
        }
        fun getOneUserInformation(userAcount: String): ArrayList<String> {
            val db = dbHelper.readableDatabase
            val userDetails = ArrayList<String>()

            // Define the selection criteria
            val selection = "$COLUMN_NUMBERACOUNT = ?"
            val selectionArgs = arrayOf(userAcount)

            // Query the database
            val cursor: Cursor = db.query(
                TABLE_NAME, // Table name
                null, // Columns (null means all columns)
                selection, // Selection (WHERE clause)
                selectionArgs, // Selection arguments
                null, // Group by
                null, // Having
                null // Order by
            )

            with(cursor) {
                if (moveToFirst()) {
                    // Extract user information
                    userDetails.add(getString(getColumnIndexOrThrow(COLUMN_NAME)))
                    userDetails.add(getString(getColumnIndexOrThrow(COLUMN_NUMBERACOUNT)))
                    userDetails.add(getDouble(getColumnIndexOrThrow(COLUMN_BALANCE)).toString())
                    userDetails.add(getString(getColumnIndexOrThrow(COLUMN_USERNAME)))
                    userDetails.add(getString(getColumnIndexOrThrow(COLUMN_TYPE_ACOUNT)))
                }
            }

            cursor.close() // Always close the cursor
            db.close() // Always close the database
            return userDetails
        }
    }