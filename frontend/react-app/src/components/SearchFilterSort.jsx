
//takes in the searchQuery state and the setSearchQuery function as peramters 
function SearchFilterSort({ searchQuery, setSearchQuery,searchForThis}){

   
    const handleSearch = (e) => {
        const value = e.target.value; //get user input
        setSearchQuery(value); //update the state
    };

    return(
        <div className="search-form"> 
            <input
                    type="text"
                    placeholder="Search:"
                    className="search-input"
                    value={searchQuery}
                    onChange={handleSearch}
                />
        </div>
    );
}

export default SearchFilterSort;