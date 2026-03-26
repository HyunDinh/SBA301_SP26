import { useNavigate, useParams } from 'react-router-dom';
import useWorkspaceDetail from '../hooks/useWorkspaceDetail';
import { ArrowLeft, Play } from 'lucide-react';
import WorkspaceSidebar from '../components/workspace/WorkspaceSidebar';
import EndpointEditor from '../components/workspace/EndpointEditor';
import LoadingSpinnerDark from '../components/common/LoadingSpinnerDark';

export default function WorkspaceDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const {
    workspaceName,
    folders,
    selectedEndpoint,
    setSelectedEndpoint,
    profile,
    loading,
    copied,
    handleCopyFullUrl,
    handleSaveEndpoint,
    toggleFolder,
    expandedFolders,
    handleCreateFolder,
    handleCreateEndpoint,
    handleEditFolder,
    handleDeleteFolder,
    handleDeleteEndpoint,
    getMethodColor,
  } = useWorkspaceDetail(id, navigate);

  if (loading) {
    return <LoadingSpinnerDark message="LOADING WORKSPACE..." />;
  }

  return (
    <div className="flex h-screen bg-[#1e1e1e] text-gray-300 overflow-hidden font-sans">
      {/* Sidebar */}
      <WorkspaceSidebar
        workspaceName={workspaceName}
        folders={folders}
        selectedEndpoint={selectedEndpoint}
        expandedFolders={expandedFolders}
        toggleFolder={toggleFolder}
        onCreateFolder={handleCreateFolder}
        onCreateEndpoint={handleCreateEndpoint}
        onEditFolder={handleEditFolder}
        onDeleteFolder={handleDeleteFolder}
        onSelectEndpoint={setSelectedEndpoint}
        onDeleteEndpoint={handleDeleteEndpoint}
        getMethodColor={getMethodColor}
        navigate={navigate}
      />

      {/* Main Editor Area */}
      <div className="flex-1 flex flex-col bg-[#1e1e1e]">
        {selectedEndpoint ? (
          <EndpointEditor
            endpoint={selectedEndpoint}
            setEndpoint={setSelectedEndpoint}
            profile={profile}
            workspaceId={id}
            copied={copied}
            onCopyFullUrl={handleCopyFullUrl}
            onSave={handleSaveEndpoint}
            getMethodColor={getMethodColor}
          />
        ) : (
          <div className="flex-1 flex flex-col items-center justify-center opacity-20 select-none">
            <Play size={80} className="text-gray-500 mb-4 stroke-1" />
            <p className="text-lg font-light tracking-widest uppercase">
              Select an endpoint to edit
            </p>
          </div>
        )}
      </div>
    </div>
  );
}