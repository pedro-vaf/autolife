/*
    Abas de navegação entre Médicos, Pacientes e Consultas

   Props:
   - activeTab: qual aba está ativa
   - setActiveTab: função para mudar a aba
*/

function Navigation({ activeTab, setActiveTab }) {
    const tabs = [
        { id: 'medicos', label: 'Médicos' },
        { id: 'pacientes', label: 'Pacientes' },
        { id: 'consultas', label: 'Consultas' }
    ];

    return (
        <div className="navigation-tabs">
            {tabs.map(tab => (
                <button
                    key={tab.id}
                    className={`tab-button ${activeTab === tab.id ? 'active' : ''}`}
                    onClick={() => setActiveTab(tab.id)}
                >
                    <span style={{ marginRight: '8px' }}></span>
                    {tab.label}
                </button>
            ))}
        </div>
    );
}

export default Navigation;