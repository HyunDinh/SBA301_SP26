import EndpointUrlBar from './EndpointUrlBar';
import ResponseSettings from './ResponseSettings';
import ResponseBodyEditor from './ResponseBodyEditor';

export default function EndpointEditor({
  endpoint,
  setEndpoint,
  profile,
  workspaceId,
  copied,
  onCopyFullUrl,
  onSave,
  getMethodColor,
}) {
  return (
    <div className="flex flex-col h-full animate-in fade-in slide-in-from-right-2 duration-300">
      {/* URL Bar */}
      <EndpointUrlBar
        endpoint={endpoint}
        setEndpoint={setEndpoint}
        profile={profile}
        workspaceId={workspaceId}
        copied={copied}
        onCopyFullUrl={onCopyFullUrl}
        onSave={onSave}
        getMethodColor={getMethodColor}
      />

      {/* Config Panels */}
      <div className="flex-1 p-6 overflow-y-auto grid grid-cols-12 gap-8">
        {/* Left: Settings */}
        <div className="col-span-4 space-y-8">
          <ResponseSettings endpoint={endpoint} setEndpoint={setEndpoint} />
        </div>

        {/* Right: Editor */}
        <div className="col-span-8">
          <ResponseBodyEditor endpoint={endpoint} setEndpoint={setEndpoint} />
        </div>
      </div>
    </div>
  );
}