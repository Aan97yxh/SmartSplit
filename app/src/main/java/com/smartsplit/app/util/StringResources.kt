package com.smartsplit.app.util

import androidx.compose.runtime.staticCompositionLocalOf

interface AppStrings {
    // General
    val appName: String
    val cancel: String
    val delete: String
    val edit: String
    val back: String
    val save: String
    val version: String
    val madeWith: String

    // Auth
    val login: String
    val register: String
    val logout: String
    val email: String
    val password: String
    val confirmPassword: String
    val fullName: String
    val name: String
    val welcomeTo: String
    val createAccount: String
    val forgotPassword: String
    val dontHaveAccount: String
    val alreadyHaveAccount: String
    val fieldRequired: String
    val invalidEmail: String
    val passwordTooShort: String
    val passwordMismatch: String
    val emailPlaceholder: String
    val passwordPlaceholder: String
    val namePlaceholder: String
    val passwordMinPlaceholder: String
    val confirmPasswordPlaceholder: String

    // Home
    val home: String
    val recentBills: String
    val searchBills: String
    val noBillsYet: String
    val noBillsDesc: String
    val total: String
    val people: String
    val settled: String
    val pending: String
    val paid: String
    val paidOf: String

    // Bill
    val setupBill: String
    val addItems: String
    val billSummary: String
    val restaurantName: String
    val date: String
    val taxPercent: String
    val serviceChargePercent: String
    val addPerson: String
    val personName: String
    val continueToItems: String
    val itemName: String
    val price: String
    val quantity: String
    val assignTo: String
    val quantityHint: String
    val addItem: String
    val editItem: String
    val deleteItem: String
    val deleteItemConfirm: String
    val noItemsYet: String
    val noItemsDesc: String
    val subtotal: String
    val viewSummary: String
    val atLeastOneItem: String
    val atLeastOnePerson: String
    val atLeastOnePersonAssigned: String
    val grandTotal: String
    val beforeTax: String
    val tax: String
    val service: String
    val yourShare: String
    val orderedItems: String
    val afterTax: String
    val markAsPaid: String
    val markAsPending: String
    val deleteBill: String
    val deleteBillConfirm: String
    val shareBill: String
    val restaurantNamePlaceholder: String
    val itemNamePlaceholder: String
    val pricePlaceholder: String
    val jastipTitle: String
    val jastipOnline: String
    val jastipOffline: String

    // Settings
    val settings: String
    val profile: String
    val editProfile: String
    val changePhoto: String
    val saveChanges: String
    val darkMode: String
    val language: String
    val notifications: String
    val about: String

    // Receipt
    val receiptGeneratedBy: String
    val receiptItems: String
    val receiptTax: String
    val receiptService: String
    val receiptTotal: String
    val receiptStatus: String
    val receiptStatusPaid: String
    val receiptStatusPending: String
}

// ── Indonesian ────────────────────────────────────────────────────

object IndonesianStrings : AppStrings {
    override val appName               = "SmartSplit"
    override val cancel                = "Batal"
    override val delete                = "Hapus"
    override val edit                  = "Edit"
    override val back                  = "Kembali"
    override val save                  = "Simpan"
    override val version               = "Versi"
    override val madeWith              = "Dibuat dengan ❤ oleh Aan & Amel"

    override val login                 = "Masuk"
    override val register              = "Daftar"
    override val logout                = "Keluar"
    override val email                 = "Email"
    override val password              = "Kata Sandi"
    override val confirmPassword       = "Konfirmasi Kata Sandi"
    override val fullName              = "Nama Lengkap"
    override val name                  = "Nama"
    override val welcomeTo             = "Selamat datang di SmartSplit"
    override val createAccount         = "Buat akun baru"
    override val forgotPassword        = "Lupa kata sandi?"
    override val dontHaveAccount       = "Belum punya akun? "
    override val alreadyHaveAccount    = "Sudah punya akun? "
    override val fieldRequired         = "Kolom ini wajib diisi"
    override val invalidEmail          = "Format email tidak valid"
    override val passwordTooShort      = "Kata sandi minimal 6 karakter"
    override val passwordMismatch      = "Kata sandi tidak cocok"

    override val home                  = "Beranda"
    override val recentBills           = "Tagihan Terbaru"
    override val searchBills           = "Cari tagihan..."
    override val noBillsYet            = "Belum Ada Tagihan"
    override val noBillsDesc           = "Mulai buat tagihan baru dengan menekan tombol +"
    override val total                 = "Total"
    override val people                = "orang"
    override val settled               = "Lunas"
    override val pending               = "Belum Lunas"
    override val paid                  = "bayar"
    override val paidOf                = "%d/%d bayar"

    override val setupBill             = "Buat Tagihan"
    override val addItems              = "Tambah Item"
    override val billSummary           = "Ringkasan Tagihan"
    override val restaurantName        = "Nama Restoran"
    override val date                  = "Tanggal"
    override val taxPercent            = "Pajak (%)"
    override val serviceChargePercent  = "Servis (%)"
    override val addPerson             = "Tambah Orang"
    override val personName            = "Nama orang"
    override val continueToItems       = "Lanjut ke Item"
    override val itemName              = "Nama Item"
    override val price                 = "Harga"
    override val quantity              = "Qty"
    override val assignTo              = "Untuk siapa?"
    override val quantityHint          = "Setiap orang membayar 1 porsi masing-masing"
    override val addItem               = "Tambah Item"
    override val editItem              = "Edit Item"
    override val deleteItem            = "Hapus Item"
    override val deleteItemConfirm     = "Yakin ingin menghapus item ini?"
    override val noItemsYet            = "Belum Ada Item"
    override val noItemsDesc           = "Tambahkan item tagihan di bawah"
    override val subtotal              = "Subtotal"
    override val viewSummary           = "Lihat Ringkasan"
    override val atLeastOneItem        = "Tambahkan minimal 1 item"
    override val atLeastOnePerson      = "Tambahkan minimal 1 orang"
    override val atLeastOnePersonAssigned = "Pilih minimal 1 orang"
    override val grandTotal            = "Total Keseluruhan"
    override val beforeTax             = "Sebelum Pajak"
    override val tax                   = "Pajak"
    override val service               = "Servis"
    override val yourShare             = "Tagihan Per Orang"
    override val orderedItems          = "Item yang dipesan:"
    override val afterTax              = "Setelah Pajak"
    override val markAsPaid            = "Tandai Lunas"
    override val markAsPending         = "Tandai Belum Lunas"
    override val deleteBill            = "Hapus Tagihan"
    override val deleteBillConfirm     = "Yakin ingin menghapus tagihan ini?"
    override val shareBill             = "Bagikan"

    override val settings              = "Pengaturan"
    override val profile               = "Profil"
    override val editProfile           = "Edit Profil"
    override val changePhoto           = "Ganti Foto"
    override val saveChanges           = "Simpan Perubahan"
    override val darkMode              = "Mode Gelap"
    override val language              = "Bahasa"
    override val notifications         = "Notifikasi"
    override val about                 = "Tentang Aplikasi"

    override val receiptGeneratedBy    = "Dibuat oleh SmartSplit • Aan & Amel"
    override val receiptItems          = "Item"
    override val receiptTax            = "Pajak"
    override val receiptService        = "Servis"
    override val receiptTotal          = "Total"
    override val receiptStatus         = "Status"
    override val receiptStatusPaid     = "✓ Lunas"
    override val receiptStatusPending  = "○ Belum Lunas"
    override val restaurantNamePlaceholder = "Masukkan nama restoran"
    override val itemNamePlaceholder   = "misal: Ayam Bakar, Es Teh"
    override val pricePlaceholder      = "15000"
    override val jastipTitle           = "Fitur Jastip (Mata Uang Asing)"
    override val jastipOnline          = "Koneksi internet aktif"
    override val jastipOffline         = "Fitur dinonaktifkan (Offline)"

    override val emailPlaceholder      = "contoh@email.com"
    override val passwordPlaceholder   = "Masukkan kata sandi Anda"
    override val namePlaceholder       = "Nama lengkap Anda"
    override val passwordMinPlaceholder     = "Minimal 6 karakter"
    override val confirmPasswordPlaceholder = "Ulangi kata sandi Anda"
}


// ── English ───────────────────────────────────────────────────────

object EnglishStrings : AppStrings {
    override val appName               = "SmartSplit"
    override val cancel                = "Cancel"
    override val delete                = "Delete"
    override val edit                  = "Edit"
    override val back                  = "Back"
    override val save                  = "Save"
    override val version               = "Version"
    override val madeWith              = "Made with ❤ by Aan & Amel"

    override val login                 = "Login"
    override val register              = "Register"
    override val logout                = "Logout"
    override val email                 = "Email"
    override val password              = "Password"
    override val confirmPassword       = "Confirm Password"
    override val fullName              = "Full Name"
    override val name                  = "Name"
    override val welcomeTo             = "Welcome to SmartSplit"
    override val createAccount         = "Create a new account"
    override val forgotPassword        = "Forgot password?"
    override val dontHaveAccount       = "Don't have an account? "
    override val alreadyHaveAccount    = "Already have an account? "
    override val fieldRequired         = "This field is required"
    override val invalidEmail          = "Invalid email format"
    override val passwordTooShort      = "Password must be at least 6 characters"
    override val passwordMismatch      = "Passwords do not match"

    override val home                  = "Home"
    override val recentBills           = "Recent Bills"
    override val searchBills           = "Search bills..."
    override val noBillsYet            = "No Bills Yet"
    override val noBillsDesc           = "Start by creating a new bill with the + button"
    override val total                 = "Total"
    override val people                = "people"
    override val settled               = "Settled"
    override val pending               = "Pending"
    override val paid                  = "paid"
    override val paidOf                = "%d/%d paid"

    override val setupBill             = "Create Bill"
    override val addItems              = "Add Items"
    override val billSummary           = "Bill Summary"
    override val restaurantName        = "Restaurant Name"
    override val date                  = "Date"
    override val taxPercent            = "Tax (%)"
    override val serviceChargePercent  = "Service (%)"
    override val addPerson             = "Add Person"
    override val personName            = "Person name"
    override val continueToItems       = "Continue to Items"
    override val itemName              = "Item Name"
    override val price                 = "Price"
    override val quantity              = "Qty"
    override val assignTo              = "Assign to?"
    override val quantityHint          = "Each person pays for 1 portion each"
    override val addItem               = "Add Item"
    override val editItem              = "Edit Item"
    override val deleteItem            = "Delete Item"
    override val deleteItemConfirm     = "Are you sure you want to delete this item?"
    override val noItemsYet            = "No Items Yet"
    override val noItemsDesc           = "Add bill items below"
    override val subtotal              = "Subtotal"
    override val viewSummary           = "View Summary"
    override val atLeastOneItem        = "Add at least 1 item"
    override val atLeastOnePerson      = "Add at least 1 person"
    override val atLeastOnePersonAssigned = "Select at least 1 person"
    override val grandTotal            = "Grand Total"
    override val beforeTax             = "Before Tax"
    override val tax                   = "Tax"
    override val service               = "Service"
    override val yourShare             = "Per Person Share"
    override val orderedItems          = "Ordered items:"
    override val afterTax              = "After Tax"
    override val markAsPaid            = "Mark as Paid"
    override val markAsPending         = "Mark as Pending"
    override val deleteBill            = "Delete Bill"
    override val deleteBillConfirm     = "Are you sure you want to delete this bill?"
    override val shareBill             = "Share"

    override val settings              = "Settings"
    override val profile               = "Profile"
    override val editProfile           = "Edit Profile"
    override val changePhoto           = "Change Photo"
    override val saveChanges           = "Save Changes"
    override val darkMode              = "Dark Mode"
    override val language              = "Language"
    override val notifications         = "Notifications"
    override val about                 = "About"

    override val receiptGeneratedBy    = "Generated by SmartSplit • Aan & Amel"
    override val receiptItems          = "Items"
    override val receiptTax            = "Tax"
    override val receiptService        = "Service"
    override val receiptTotal          = "Total"
    override val receiptStatus         = "Status"
    override val receiptStatusPaid     = "✓ Settled"
    override val receiptStatusPending  = "○ Pending"
    override val restaurantNamePlaceholder = "Enter restaurant name"
    override val itemNamePlaceholder   = "e.g., Grilled Chicken, Iced Tea"
    override val pricePlaceholder      = "15000"
    override val jastipTitle           = "Jastip Feature (Foreign Currency)"
    override val jastipOnline          = "Active internet connection"
    override val jastipOffline         = "Feature disabled (Offline)"

    override val emailPlaceholder      = "example@email.com"
    override val passwordPlaceholder   = "Enter your password"
    override val namePlaceholder       = "Your full name"
    override val passwordMinPlaceholder     = "Minimum 6 characters"
    override val confirmPasswordPlaceholder = "Repeat your password"
}

val LocalStrings = staticCompositionLocalOf<AppStrings> { IndonesianStrings }