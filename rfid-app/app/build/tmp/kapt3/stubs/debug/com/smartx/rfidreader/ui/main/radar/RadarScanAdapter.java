package com.smartx.rfidreader.ui.main.radar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010#\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0017\u0018B\u0015\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\n\u001a\u00020\u000bJ\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\rJ\u001c\u0010\u000e\u001a\u00020\u000b2\n\u0010\u000f\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u001c\u0010\u0012\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0011H\u0016J\u0006\u0010\u0016\u001a\u00020\u000bR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter$ViewHolder;", "existingEpcs", "", "", "(Ljava/util/Set;)V", "selected", "", "clearSelection", "", "getSelectedEpcs", "", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "selectAll", "DiffCallback", "ViewHolder", "app_debug"})
public final class RadarScanAdapter extends androidx.recyclerview.widget.ListAdapter<com.smartx.rfidreader.core.reader.RfidTag, com.smartx.rfidreader.ui.main.radar.RadarScanAdapter.ViewHolder> {
    
    /**
     * EPCs já adicionados como targets — mostrados como desabilitados
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> existingEpcs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> selected = null;
    
    public RadarScanAdapter(@org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> existingEpcs) {
        super(null);
    }
    
    /**
     * Retorna os EPCs atualmente marcados pelo usuário (excluindo já existentes)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getSelectedEpcs() {
        return null;
    }
    
    /**
     * Marca todas as tags elegíveis (não existentes) como selecionadas
     */
    public final void selectAll() {
    }
    
    /**
     * Desmarca todas
     */
    public final void clearSelection() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.smartx.rfidreader.ui.main.radar.RadarScanAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.ui.main.radar.RadarScanAdapter.ViewHolder holder, int position) {
    }
    
    public RadarScanAdapter() {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "()V", "areContentsTheSame", "", "old", "new", "areItemsTheSame", "app_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.smartx.rfidreader.core.reader.RfidTag> {
        
        public DiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.RfidTag old, @org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.RfidTag p1_54480) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.RfidTag old, @org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.RfidTag p1_54480) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/smartx/rfidreader/databinding/ItemRadarScanTagBinding;", "(Lcom/smartx/rfidreader/ui/main/radar/RadarScanAdapter;Lcom/smartx/rfidreader/databinding/ItemRadarScanTagBinding;)V", "bind", "", "tag", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.smartx.rfidreader.databinding.ItemRadarScanTagBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.databinding.ItemRadarScanTagBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.smartx.rfidreader.core.reader.RfidTag tag) {
        }
    }
}