//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.s11.ng.jan.capit01_mobdeve.R
//
//class EmergencyContactsActivity : AppCompatActivity(), EmergencyContactAdapter.ItemClickListener {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: EmergencyContactAdapter
//    private val emergencyContacts = mutableListOf<EmergencyContact>()
//
//    private val REQUEST_CALL_PERMISSION = 1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dashboard_rt)
//
//        recyclerView = findViewById(R.id.emergency_contacts_recycler)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        adapter = EmergencyContactAdapter(this)
//        recyclerView.adapter = adapter
//
//        // Add some sample emergency contacts
//        emergencyContacts.add(EmergencyContact("John Doe", "1234567890"))
//        emergencyContacts.add(EmergencyContact("Jane Doe", "0987654321"))
//        adapter.submitList(emergencyContacts)
//    }
//
//    override fun onItemClick(emergencyContact: EmergencyContact) {
//        val callIntent = Intent(Intent.ACTION_CALL)
//        callIntent.data = Uri.parse("tel:${emergencyContact.phoneNumber}")
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            startActivity(callIntent)
//        } else {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == REQUEST_CALL_PERMISSION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
//
//data class EmergencyContact(val name: String, val phoneNumber: String)