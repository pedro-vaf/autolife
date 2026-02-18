/*
   Props:
   - isOpen: boolean - se o modal está aberto
   - onClose: function - função para fechar
   - title: string - título do modal
   - icon: string - emoji do ícone
   - children: componentes filhos a renderizar dentro
*/
function Modal({ isOpen, onClose, title, icon, children }) {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <div className="modal-header-content">
                        <div className="modal-icon medico">
                            <span style={{ fontSize: '24px' }}>{icon}</span>
                        </div>
                        <div className="modal-title">
                            <h2>{title}</h2>
                            <p>Preencha os campos abaixo</p>
                        </div>
                    </div>
                    <button className="modal-close" onClick={onClose}>
                        <span style={{ fontSize: '20px' }}>✕</span>
                    </button>
                </div>
                <div className="modal-body">
                    {children}
                </div>
            </div>
        </div>
    );
}

export default Modal;