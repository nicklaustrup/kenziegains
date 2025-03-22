// Frontend/src/api/config.js
const getApiBaseUrl = () => {
    // In production, make API calls to the same domain that's serving the page
    if (window.location.hostname !== 'localhost') {
      // Use relative URLs - they will automatically use the current domain
      return '';
    }
    
    // For local development
    return 'http://localhost:5001';
  };
  
  export const API_BASE_URL = getApiBaseUrl();