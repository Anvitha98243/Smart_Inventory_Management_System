package com.inventory.controller;
import com.inventory.dto.*;
import com.inventory.entity.User;
import com.inventory.repository.UserRepository;
import com.inventory.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private ProductService productService;
    @Autowired private TransactionService txService;
    @Autowired private StockRequestService reqService;
    @Autowired private NotificationService notifService;
    @Autowired private ReportService reportService;
    @Autowired private UserRepository userRepo;

    // ── Products ──────────────────────────────────────────────────────────────
    @GetMapping("/products")
    public ResponseEntity<ApiResponse> getProducts(Authentication a) { return ResponseEntity.ok(productService.getAll(a.getName())); }

    @GetMapping("/products/low-stock")
    public ResponseEntity<ApiResponse> lowStock(Authentication a) { return ResponseEntity.ok(productService.lowStock(a.getName())); }

    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse> search(@RequestParam String keyword, Authentication a) { return ResponseEntity.ok(productService.search(a.getName(),keyword)); }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable Long id, Authentication a) { return ResponseEntity.ok(productService.getById(id,a.getName())); }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductRequest r, Authentication a) { return ResponseEntity.ok(productService.add(r,a.getName())); }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest r, Authentication a) { return ResponseEntity.ok(productService.update(id,r,a.getName())); }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id, Authentication a) { return ResponseEntity.ok(productService.delete(id,a.getName())); }

    // ── Transactions ──────────────────────────────────────────────────────────
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse> getTransactions(Authentication a) { return ResponseEntity.ok(txService.getAll(a.getName())); }

    @GetMapping("/transactions/product/{id}")
    public ResponseEntity<ApiResponse> getByProduct(@PathVariable Long id, Authentication a) { return ResponseEntity.ok(txService.getByProduct(id,a.getName())); }

    @GetMapping("/transactions/range")
    public ResponseEntity<ApiResponse> getByRange(@RequestParam String start, @RequestParam String end, Authentication a) { return ResponseEntity.ok(txService.getByRange(a.getName(),start,end)); }

    @PostMapping("/transactions")
    public ResponseEntity<ApiResponse> addTransaction(@Valid @RequestBody TransactionRequest r, Authentication a) { return ResponseEntity.ok(txService.create(r,a.getName())); }

    // ── Stock Requests ────────────────────────────────────────────────────────
    @GetMapping("/requests")
    public ResponseEntity<ApiResponse> getRequests(Authentication a) { return ResponseEntity.ok(reqService.getForAdmin(a.getName())); }

    @GetMapping("/requests/pending")
    public ResponseEntity<ApiResponse> getPending(Authentication a) { return ResponseEntity.ok(reqService.getPendingForAdmin(a.getName())); }

    @PutMapping("/requests/{id}/process")
    public ResponseEntity<ApiResponse> process(@PathVariable Long id, @RequestBody ProcessRequestDTO dto, Authentication a) { return ResponseEntity.ok(reqService.process(id,dto,a.getName())); }

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> dashboard(Authentication a) {
        User admin = userRepo.findByUsername(a.getName()).orElse(null);
        if (admin==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        DashboardStats stats = txService.getDashboardStats(a.getName());
        stats.setPendingRequests(reqService.pendingCount(admin.getId()));
        return ResponseEntity.ok(new ApiResponse(true,"OK",stats));
    }

    // ── Notifications ─────────────────────────────────────────────────────────
    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse> getNotifs(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(new ApiResponse(true,"OK",notifService.getAll(u.getId())));
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<ApiResponse> unreadCount(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(new ApiResponse(true,"OK",notifService.unreadCount(u.getId())));
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long id, Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(notifService.markRead(id,u.getId()));
    }

    @PutMapping("/notifications/read-all")
    public ResponseEntity<ApiResponse> markAllRead(Authentication a) {
        User u = userRepo.findByUsername(a.getName()).orElse(null);
        if (u==null) return ResponseEntity.ok(new ApiResponse(false,"Not found",null));
        return ResponseEntity.ok(notifService.markAllRead(u.getId()));
    }

    // ── Reports ───────────────────────────────────────────────────────────────
    @GetMapping("/reports/excel")
    public void excel(@RequestParam(defaultValue="") String start, @RequestParam(defaultValue="") String end,
                      @RequestParam(defaultValue="ALL") String type, Authentication a, HttpServletResponse res) throws Exception {
        String s = start.isBlank() ? LocalDate.now().withDayOfMonth(1).toString() : start;
        String e = end.isBlank() ? LocalDate.now().toString() : end;
        byte[] data = reportService.excel(a.getName(),s,e,type);
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition","attachment; filename=report.xlsx");
        res.getOutputStream().write(data);
    }

    @GetMapping("/reports/pdf")
    public void pdf(@RequestParam(defaultValue="") String start, @RequestParam(defaultValue="") String end,
                    @RequestParam(defaultValue="ALL") String type, Authentication a, HttpServletResponse res) throws Exception {
        String s = start.isBlank() ? LocalDate.now().withDayOfMonth(1).toString() : start;
        String e = end.isBlank() ? LocalDate.now().toString() : end;
        byte[] data = reportService.pdf(a.getName(),s,e,type);
        res.setContentType("application/pdf");
        res.setHeader("Content-Disposition","attachment; filename=report.pdf");
        res.getOutputStream().write(data);
    }
}
